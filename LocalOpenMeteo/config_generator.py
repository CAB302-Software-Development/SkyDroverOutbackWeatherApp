import os.path
import boto3
from botocore import UNSIGNED
from botocore.client import Config
import yaml

def list_all_folders(bucket_name, prefix='', delimiter='/',remove_prefix=True,remove_suffix=True):
    # Create an S3 client with unsigned requests (no credentials needed)
    s3 = boto3.client('s3', config=Config(signature_version=UNSIGNED))

    paginator = s3.get_paginator('list_objects_v2')
    response_iterator = paginator.paginate(Bucket=bucket_name, Prefix=prefix, Delimiter=delimiter)

    directories = set()

    for page in response_iterator:
        if 'CommonPrefixes' in page:
            for obj in page['CommonPrefixes']:
                key = obj['Prefix']
                if remove_prefix:
                    key = key[len(prefix):]
                if remove_suffix:
                    key = key[:-1]
                directories.add(key)

    print("Directories:")
    for directory in sorted(directories):
        print(directory)

    return directories

bucket_name = 'openmeteo'



models = list_all_folders(bucket_name, prefix='data/')
# Convert to list
models = list(models)

#DEBUG limit the number of models
#models = models[:2]

# Create a yaml file to store the enabled models
if not os.path.exists('models.yaml'):
    with open('models.yaml', 'w') as f:
        yaml.dump({model: False for model in models}, f)

# Load the enabled models
with open('models.yaml', 'r') as f:
    existing_models = yaml.load(f, Loader=yaml.FullLoader)
    # Add new models
    for model in models:
        if not model in existing_models:
            existing_models[model] = False

# Save the enabled models
with open('models.yaml', 'w') as f:
    yaml.dump(existing_models, f)

# Filter the models to only include the enabled ones
models = [model for model,enabled in existing_models.items() if enabled]

# Get the variables for each model
models = {model: list_all_folders(bucket_name, prefix=f'data/{model}/') for model in models}

#load the template config file
with open('template-docker-compose.yml','r') as f:
    config = yaml.load(f, Loader=yaml.FullLoader)

# write a sync service for each model
envs = {
    "OPEN_METEO_DEFAULT_MAX_AGE_DAYS": 3,
    "OPEN_METEO_DEFAULT_REPEAT_INTERVAL": 5,
    "OPEN_METEO_DEFAULT_CONCURRENT": 4,
    "API_BIND": "0.0.0.0:8080"
}
for model,variables in models.items():
    command = 'sync'

    # Add the models
    env_name = f'OPEN_METEO_{model.upper()}_MODELS'
    envs[env_name] = model
    command += ' ' + '${' + env_name + '}'

    # Add the variables
    env_name = f'OPEN_METEO_{model.upper()}_VARIABLES'
    envs[env_name] = ','.join(variables)
    command += ' ' + '${' + env_name + '}'

    # Add the past days
    env_name = f'OPEN_METEO_{model.upper()}_MAX_AGE_DAYS'
    envs['#'+env_name] = envs["OPEN_METEO_DEFAULT_MAX_AGE_DAYS"]
    command += ' --past-days ' + '${' + env_name + ':-$OPEN_METEO_DEFAULT_MAX_AGE_DAYS}'

    # Add the repeat interval
    env_name = f'OPEN_METEO_{model.upper()}_REPEAT_INTERVAL'
    envs['#'+env_name] = envs["OPEN_METEO_DEFAULT_REPEAT_INTERVAL"]
    command += ' --repeat-interval ' + '${' + env_name + ':-$OPEN_METEO_DEFAULT_REPEAT_INTERVAL}'

    # Add the concurrent
    env_name = f'OPEN_METEO_{model.upper()}_CONCURRENT'
    envs['#'+env_name] = envs["OPEN_METEO_DEFAULT_CONCURRENT"]
    command += ' --concurrent ' + '${' + env_name + ':-$OPEN_METEO_DEFAULT_CONCURRENT}'

    service = {
        'image': 'ghcr.io/open-meteo/open-meteo',
        'container_name': f'open-meteo-sync-{model}',
        'command': command,
        'volumes': [
            'data:/app/data'
        ],
        'restart': 'always',
        'env_file': '.env',
        'deploy': {
            'resources': {
                'limits': {
                    'memory': '512M'
                }
            }
        }
    }

    # Add the service
    config['services'][f'open-meteo-sync-{model}'] = service

print(config)

#dump the envs
with open('.env','w') as f:
    for k,v in envs.items():
        f.write(f'{k}={v}\n')

#dump the config
with open('docker-compose.yml', 'w') as f:
    yaml.dump(config, f)