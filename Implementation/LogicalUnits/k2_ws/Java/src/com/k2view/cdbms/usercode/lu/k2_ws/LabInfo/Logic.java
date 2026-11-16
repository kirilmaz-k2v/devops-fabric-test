/////////////////////////////////////////////////////////////////////////
// Lab web service: shows which jar each library class is loaded from at runtime
/////////////////////////////////////////////////////////////////////////

package com.k2view.cdbms.usercode.lu.k2_ws.LabInfo;

import com.k2view.cdbms.shared.user.WebServiceUserCode;
import com.k2view.fabric.api.endpoint.Endpoint.MethodType;
import com.k2view.fabric.api.endpoint.Endpoint.Produce;
import com.k2view.fabric.api.endpoint.Endpoint.webService;
import com.customer.utils.Masker;

import java.util.LinkedHashMap;
import java.util.Map;

public class Logic extends WebServiceUserCode {

    @webService(path = "lab/libs", verb = MethodType.GET, produce = Produce.JSON)
    public static Object wsLabLibs() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("deployedArtifact", artifactInfo());
        out.put("mask", Masker.mask("4111111111111111"));
        out.put("title", Masker.title("hello fabric"));
        out.put("customerUtils", Masker.VERSION);
        out.put("luClassLoader", String.valueOf(Logic.class.getClassLoader()));
        out.put("customer-utils", source("com.customer.utils.Masker"));
        out.put("commons-text", source("org.apache.commons.text.WordUtils"));
        out.put("commons-lang3", source("org.apache.commons.lang3.StringUtils"));
        out.put("gson (provided scope)", source("com.google.gson.Gson"));
        return out;
    }

    /** META-INF/fabric-artifact.properties, kept in ludb.jar by deploy-artifacts.sh --artifact-file */
    private static String artifactInfo() {
        try (java.io.InputStream in = Logic.class.getClassLoader().getResourceAsStream("META-INF/fabric-artifact.properties")) {
            if (in == null) return "none (not deployed from a single LU artifact)";
            java.util.Properties p = new java.util.Properties();
            p.load(in);
            return p.getProperty("lu.name") + " " + p.getProperty("lu.version") + " built " + p.getProperty("build.time");
        } catch (Exception e) {
            return "error: " + e;
        }
    }

    private static String source(String className) {
        try {
            Class<?> c = Class.forName(className, false, Logic.class.getClassLoader());
            String v = c.getPackage() == null ? null : c.getPackage().getImplementationVersion();
            return c.getProtectionDomain().getCodeSource().getLocation() + (v == null ? "" : " (version " + v + ")");
        } catch (Throwable t) {
            return "NOT VISIBLE: " + t;
        }
    }
}
