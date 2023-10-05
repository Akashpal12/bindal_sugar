package in.co.vibrant.bindalsugar.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import java.io.File;

public class FileUtils {

    public static File createDirectory(Context context, String folderName) {
        File directory;

        // Check if the device is running Android 10 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Create directory in app-specific external storage
            directory = new File(context.getExternalFilesDir(null), folderName);
        } else {
            // Create directory in public external storage (legacy approach)
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            directory = new File(externalStorageDirectory, folderName);
        }

        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                // Directory created successfully
            } else {
                // Directory creation failed
            }
        } else {
            // Directory already exists
        }

        return directory;
    }
}
