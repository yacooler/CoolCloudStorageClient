package frames;


import fileobjects.FileInformation;
import lombok.Getter;
import lombok.Setter;

public class BaseDataFrame extends BaseFrame {

    @Getter @Setter
    protected int currentFrame = -1;

    @Getter @Setter
    private int lastFrame = -1;

    @Getter @Setter
    private int dataOffset = 0;

    @Getter @Setter
    private FileInformation fileInformation;

    @Getter @Setter
    private boolean initialized;

    public BaseDataFrame() {
    }

}
