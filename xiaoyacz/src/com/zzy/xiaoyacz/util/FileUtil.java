package com.zzy.xiaoyacz.util;

import android.os.Environment;

public class FileUtil {
	public static boolean IsExternalStorageAvailableAndWriteable() {
		boolean externalStorageAvailable = false;
		boolean externalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// ---you can read and write the media---
			externalStorageAvailable = externalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// ---you can only read the media---
			externalStorageAvailable = true;
			externalStorageWriteable = false;
		} else {
			// ---you cannot read nor write the media---
			externalStorageAvailable = externalStorageWriteable = false;
		}
		return externalStorageAvailable && externalStorageWriteable;
	}

}
