package com.karatitza.project;

import java.io.File;

public interface PathConstants {

    String PROJECT_PRINT_TEMP_PATH = String.format("%sprint%stemp", File.separator, File.separator);
    String PROJECT_PRINT_PATH = String.format("%sprint", File.separator);
}
