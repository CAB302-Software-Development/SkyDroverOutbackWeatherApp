package cab302softwaredevelopment.outbackweathertrackerapplication.utils;

import com.gluonhq.attach.storage.StorageService;
import java.io.File;
import java.util.Optional;

public class LocalStorageService implements StorageService {
    @Override
    public Optional<File> getPrivateStorage() {
        File file = new File(".");
        return Optional.of(file);
    }

    @Override
    public Optional<File> getPublicStorage(String subdirectory) {
        return Optional.empty();
    }

    @Override
    public boolean isExternalStorageWritable() {
        return false;
    }

    @Override
    public boolean isExternalStorageReadable() {
        return false;
    }
}
