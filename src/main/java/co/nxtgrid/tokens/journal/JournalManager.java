package co.nxtgrid.tokens.journal;

import jakarta.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.stream.Collectors;

public class JournalManager {

    private String pathToJournal;
    private BufferedWriter bufferedWriter;
    private FileWriter fileWriter;

    public JournalManager(String pathToJournal) {
        setPathToJournal(pathToJournal);
    }

    public String getPathToJournal() {
        return pathToJournal;
    }

    public void setPathToJournal(String pathToJournal) {
        this.pathToJournal = pathToJournal;
    }

    public void initialize() throws IOException {
        fileWriter = new FileWriter(pathToJournal, true);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    public boolean open() {
        return (null != bufferedWriter);
    }

    public void write(String line, String type) throws IOException {
        bufferedWriter.write(String.format("%s\t%s ----%s\n",
                new SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
                        .format(new Date()), type, line));
        bufferedWriter.flush();
    }

    public void clear() throws IOException {
        FileWriter clearFileWriter = new FileWriter(pathToJournal, false);
        bufferedWriter = new BufferedWriter(clearFileWriter);
        bufferedWriter.write("");
        bufferedWriter.flush();
        bufferedWriter.close();
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    public String read() throws IOException {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        fileReader = new FileReader(pathToJournal);
        bufferedReader = new BufferedReader(fileReader);
        String line, result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        bufferedReader.close();
        fileReader.close();
        return result;
    }

    public String serialize(HttpServletRequest request) {
        String res = "\nPath = " + request.getPathInfo() + ", \n" +
                        "Method = " + request.getMethod()  + ", \n" +
                        "AuthType = " + request.getAuthType() + ", \n" +
                        "QueryString = " + request.getQueryString() + ", \n" +
                        "RequestURI = " + request.getRequestURI() + ", \n";

        String paramsString = "\nParams:\n";
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String,String[]> entry : params.entrySet()) {
            paramsString += "Key = " + entry.getKey()
                    + ", Value = "
                    + Arrays.stream(entry.getValue()).collect(Collectors.joining(","))
                    +"\n";
        }
        res += paramsString;

        String headers = "\nHeaders:\n";
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headers += "Key = " +  header
                    + ", Value = "
                    + request.getHeader(header)
                    +"\n";
        }
        res += headers;
        return res;
    }

    public String serialize(HttpServletRequest request, Object values)
        throws IllegalAccessException {
        String res = serialize(request);

        String content = "\nContent:\n";
        Field[] fields = values.getClass().getDeclaredFields();

        for(Field field: fields) {
            field.setAccessible(true);
            content += "Key = " +  field.getName()
                    + ", Value = "
                    + field.get(values)
                    +"\n";
        }
        res += content;
        return res;
    }

    public String serialize(Object values)
            throws IllegalAccessException {
        String res = "";

        String content = "\nContent:\n";
        Field[] fields = values.getClass().getDeclaredFields();

        for(Field field: fields) {
            field.setAccessible(true);
            content += "Key = " +  field.getName()
                    + ", Value = "
                    + field.get(values)
                    +"\n";
        }
        res += content;
        return res;
    }

    public void close() throws IOException {
        if (bufferedWriter != null)
            bufferedWriter.close();

        if (fileWriter != null)
            fileWriter.close();
    }
}

