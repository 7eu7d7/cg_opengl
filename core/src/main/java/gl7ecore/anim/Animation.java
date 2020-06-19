package gl7ecore.anim;

import java.util.HashMap;
import java.util.List;

public class Animation {

    private int currentFrame;

    private List<AnimatedFrame> frames;

    public HashMap<String,FrameRange> sub_anims=new HashMap<String,FrameRange>();

    private String name;
    
    private double duration;

    public Animation(String name, List<AnimatedFrame> frames, double duration) {
        this.name = name;
        this.frames = frames;
        currentFrame = 0;
        this.duration = duration;
    }

    public AnimatedFrame getCurrentFrame() {
        return this.frames.get(currentFrame);
    }

    public double getDuration() {
        return this.duration;        
    }

    /*public double getDurationNow() {
        return this.duration*()/();
    }*/

    public List<AnimatedFrame> getFrames() {
        return frames;
    }

    public String getName() {
        return name;
    }

    public AnimatedFrame getNextFrame() {
        nextFrame();
        return this.frames.get(currentFrame);
    }

    public void nextFrame() {
        int nextFrame = currentFrame + 1;
        if (nextFrame > frames.size() - 1) {
            currentFrame = 0;
        } else {
            currentFrame = nextFrame;
        }
    }

    public class FrameRange{
        public int start;
        public int end;
        public FrameRange(int start,int end){
            this.start=start;
            this.end=end;
        }
    }

}
