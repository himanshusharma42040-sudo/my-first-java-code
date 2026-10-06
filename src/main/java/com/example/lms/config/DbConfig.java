package com.example.lms.config;

public final class DbConfig {
    private DbConfig() {}
    public static String url() {
        return value("LMS_DB_URL", "lms.db.url",
            "jdbc:mysql://localhost:3306/lms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
    }
    public static String user() {
        String v=value("LMS_DB_USER","lms.db.user",null);
        if(v==null||v.isBlank()) throw new IllegalStateException("Set LMS_DB_USER or -Dlms.db.user=YOUR_USER");
        return v;
    }
    public static String password() {
        String v=value("LMS_DB_PASSWORD","lms.db.password",null);
        if(v==null) throw new IllegalStateException("Set LMS_DB_PASSWORD or -Dlms.db.password=YOUR_PASSWORD");
        return v;
    }
    private static String value(String env,String prop,String fallback) {
        String e=System.getenv(env);
        if(e!=null&&!e.isBlank()) return e;
        String p=System.getProperty(prop);
        if(p!=null&&!p.isBlank()) return p;
        return fallback;
    }
}
