package fps.reader;

import javax.swing.*;
import java.awt.*;

public class FPSViewer {
    private JPanel panel1;
    private JProgressBar progressBar1;
    private JProgressBar progressBar2;
    private JProgressBar gpuvram;
    private JProgressBar temperature;

    public FPSViewer(){

    }

    public JProgressBar getProgressBar1() {
        return progressBar1;
    }

    public JProgressBar getProgressBar2() {
        return progressBar2;
    }

    public JProgressBar getGpuvram() {
        return gpuvram;
    }

    public JProgressBar getTemperature() {
        return temperature;
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
