package cab302softwaredevelopment.outbackweathertrackerapplication.utils;

import com.gluonhq.attach.storage.StorageService;
import com.gluonhq.attach.util.impl.ServiceFactory;


import java.util.Optional;

public class LocalStorageServiceFactory implements ServiceFactory<StorageService> {
    @Override
    public Class<StorageService> getServiceType() {
        return StorageService.class;
    }

    @Override
    public Optional<StorageService> getInstance() {
        return Optional.of(new LocalStorageService());
    }
}
