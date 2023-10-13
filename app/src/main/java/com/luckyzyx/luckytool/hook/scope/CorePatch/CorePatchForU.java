package com.luckyzyx.luckytool.hook.scope.CorePatch;

import java.lang.reflect.InvocationTargetException;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class CorePatchForU extends CorePatchForT {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        super.handleLoadPackage(loadPackageParam);
        
        findAndHookMethod("com.android.server.pm.PackageManagerServiceUtils", loadPackageParam.classLoader,
                "checkDowngrade",
                "com.android.server.pm.pkg.AndroidPackage",
                "android.content.pm.PackageInfoLite",
                new ReturnConstant(prefs, "downgrade", null));
    }
}
