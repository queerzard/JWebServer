package com.github.sebyplays.jwebserver.utils;

import com.github.sebyplays.jwebserver.JWebServer;
import com.github.sebyplays.jwebserver.utils.enums.HashType;
import lombok.SneakyThrows;

import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;

/**
 * It's a utility class that contains a bunch of methods that are used throughout the project
 */
public class Utilities {

    /**
     * It takes a path to a file in the resources folder and returns a File object
     *
     * @param path The path to the file you want to get.
     * @return A file object
     */
    public static File getFileFromResource(String path) {
        File file;
        try {
            file = new File(Utilities.class.getResource(path).getFile());
            if(file.exists())
                return file;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * If the file is in the cache, return it, otherwise, if the file exists in the html resource directory, add it to the
     * cache and return it, otherwise, if the file exists in the resource directory, add it to the cache and return it,
     * otherwise, return null
     *
     * @param path The path to the file.
     * @return A HtmlModel object
     */
    @SneakyThrows
    public static HtmlModel getCachedHTMLModel(String path) {
        File file;
        if(JWebServer.pageCache.containsKey(path))
            return JWebServer.pageCache.get(path);
        if((file = getFileFromHtmlResourceDir(path)).exists() || ((file = getFileFromResource(path)) != null && file.exists()))
            JWebServer.pageCache.put(path, new HtmlModel(new String(Files.readAllBytes(file.toPath()))));
        else
            return null;
        return getCachedHTMLModel(path);
    }

    /**
     * Given a path, return a file object that points to the file at that path in the html resource directory.
     *
     * @param path The path to the file, relative to the rootHtmlDir.
     * @return A file object
     */
    public static File getFileFromHtmlResourceDir(String path) {
        return new File(JWebServer.rootHtmlDir + "/" + path);
    }

    /**
     * "Get the HTML content of a file in the resources folder, and cache it."
     *
     * @param path The path to the HTML file.
     * @return The raw HTML content of the cached HTML model.
     */
    @SneakyThrows
    public static String getCachedHTMLResource(String path) {
        return getCachedHTMLModel(path).getRawHtmlContent();
    }

    /**
     * It takes a package name as a string, and returns an array of all the classes in that package
     *
     * @param packageName The package name for classes found inside the base directory
     * @return An array of classes.
     */
    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList();
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList classes = new ArrayList();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }

    /**
     * It takes a directory and a package name, and returns a list of all the classes in that directory
     *
     * @param directory The directory to search for classes
     * @param packageName The package name of the class you want to find.
     * @return A list of classes in the package.
     */
    public static List findClasses(File directory, String packageName) {
        List classes = new ArrayList();
        if (!directory.exists())
            return classes;
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                try {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

    /**
     * If the class is annotated with the annotation, return the annotation, otherwise return null.
     *
     * @param clazz The class to check for the annotation
     * @param annotation The annotation class to look for.
     * @return Annotation
     */
    public static Annotation getAnnotation(Class clazz, Class annotation){
        if(clazz.isAnnotationPresent(annotation))
            return clazz.getAnnotation(annotation);
        return null;
    }

    /**
     * It takes a hash type and a string, and returns the hash of the string
     *
     * @param hashType The type of hash you want to use.
     * @param inputHash The string you want to hash.
     * @return A string of the hash
     */
    public static String hash(HashType hashType, String inputHash) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance(hashType.toString().replace("_", "-"));
        messageDigest.update(inputHash.getBytes());

        byte[] digest = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (byte byt : digest)
            stringBuffer.append(String.format("%02x", byt & 0xff));
        return stringBuffer.toString();
    }

    /**
     * It compiles a java file, and returns the compiled class file
     *
     * @param inputFile The file to compile
     * @return The file that was compiled.
     */
    @SneakyThrows
    public static File compile(File inputFile) {
        int exitCode = 1;
        File classFile, pkgDir;
        String code = new String(Files.readAllBytes(inputFile.toPath()));
        String className = getClassName(code).replaceAll(" ", "");
        String packageName = getPackage(code);
        System.out.println(packageName);
        if(packageName.contains("."))
            packageName = packageName.replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + Matcher.quoteReplacement(File.separator);
        pkgDir = new File(JWebServer.rootClassDir.getAbsolutePath() + Matcher.quoteReplacement(File.separator) + packageName + Matcher.quoteReplacement(File.separator));
        classFile = new File(pkgDir.getAbsolutePath() + Matcher.quoteReplacement(File.separator) + className + ".class");

        if(!pkgDir.exists())
            pkgDir.mkdirs();

        if(classFile.exists())
            classFile.delete();

        if(inputFile.getName().endsWith(".java"))
            exitCode = ToolProvider.getSystemJavaCompiler().run(null, null, null, inputFile.getAbsolutePath());
        File endFile = null;
        if(exitCode == 0) {
            File finalFile = new File(inputFile.getParentFile().getAbsolutePath() + Matcher.quoteReplacement(File.separator) + className + ".class");
            endFile = Files.copy(finalFile.toPath(), new File(pkgDir.getAbsolutePath() + Matcher.quoteReplacement(File.separator) + className + ".class").toPath()).toFile();
            if(!checksum(classFile, MessageDigest.getInstance("MD5")).equals(checksum(finalFile, MessageDigest.getInstance("MD5"))))
                exitCode = 2;
            finalFile.delete();
        }
        return endFile;
    }

    /**
     * It takes a string of code and returns the name of the class
     *
     * @param code The code to be parsed.
     * @return The class name of the code.
     */
    public static String getClassName(String code){
        String c = "";
        try {
            c = code.substring(code.indexOf("class") + 6, code.indexOf("{"));
        } catch (Exception e) {
            return "";
        }
        return c;
    }

    /**
     * It takes a string of code and returns the package name
     *
     * @param code The code to be parsed.
     * @return The package name of the class.
     */
    public static String getPackage(String code){
        String p = "";
        try {
            p = code.substring(code.indexOf("package") + 8, code.indexOf(";"));
        } catch (Exception e) {
            return "";
        }
        return p;
    }

    /**
     * It takes a file and a MessageDigest object, and returns the checksum of the file
     *
     * @param file The file to calculate the checksum for.
     * @param digest The MessageDigest object to use.
     * @return The checksum of the file.
     */
    @SneakyThrows
    public static String checksum(File file, MessageDigest digest) {
        FileInputStream fis = new FileInputStream(file);
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        while ((bytesCount = fis.read(byteArray)) != -1)
            digest.update(byteArray, 0, bytesCount);
        fis.close();
        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        return sb.toString();
    }



}
