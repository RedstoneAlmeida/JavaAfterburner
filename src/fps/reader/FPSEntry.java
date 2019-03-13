package fps.reader;

public class FPSEntry {

    private int fps = 0;
    private int vram = 0;
    private int gpuUse = 0;
    private int gpuFreq = 0;
    private int gpuTemp = 60;

    private int cpuUse = 0;

    private int media = 0;

    public FPSEntry(){

    }

    public int getGpuTemp() {
        return gpuTemp;
    }

    public void setGpuTemp(int gpuTemp) {
        this.gpuTemp = gpuTemp;
    }

    public int getMedia() {
        return media;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public int getCpuUse() {
        return cpuUse;
    }

    public int getFps() {
        return fps;
    }

    public int getGpuFreq() {
        return gpuFreq;
    }

    public int getGpuUse() {
        return gpuUse;
    }

    public int getVram() {
        return vram;
    }

    public void setCpuUse(int cpuUse) {
        this.cpuUse = cpuUse;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setGpuFreq(int gpuFreq) {
        this.gpuFreq = gpuFreq;
    }

    public void setGpuUse(int gpuUse) {
        this.gpuUse = gpuUse;
    }

    public void setVram(int vram) {
        this.vram = vram;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FPS:").append(fps).append("\n");
        builder.append("GPU:").append(gpuUse).append("%\n");
        builder.append("VRAM:").append(vram).append("\n");
        builder.append("TEMP:").append(gpuTemp).append("\n");
        return builder.toString();
    }
}
