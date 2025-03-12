package com.asluax.lease.common.constant;

public class LoginUserContext {

    private static final ThreadLocal<LoginUser> userThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<LoginUserApp> userAppThreadLocal = new ThreadLocal<>();

    public static void setLoginUser(LoginUser loginUser) {
        userThreadLocal.set(loginUser);
    }
    public static void setLoginUserApp(LoginUserApp LoginUserApp) {
        userAppThreadLocal.set(LoginUserApp);
    }

    public static LoginUser getLoginUser() {
        return userThreadLocal.get();
    }
    public static LoginUserApp getLoginUserApp() {
        return userAppThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }
    public static void clearApp() {
        userAppThreadLocal.remove();
    }
}