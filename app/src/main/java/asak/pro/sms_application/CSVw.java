package asak.pro.sms_application;

/**
 * Created by root on 1/25/16.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVw {

    private PrintWriter csvWriter;
    private File file;

    public CSVw(File file) {
        this.file = file;
    }

    public void write_UserId(String number) {
        try {
            csvWriter = new PrintWriter(new FileWriter(file, true));
            csvWriter.print(number);
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}