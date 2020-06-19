package gl7ecore.anim;

import java.util.HashMap;
import java.util.List;

public class Animation {

    private int currentFrame;

    private List<AnimatedFrame> frames;

    public HashMap<String,FrameRange> sub_anims=new HashMap<String,FrameRange>();

    FrameRange fs_now;
    String name_now;

    public OnAnimationOverListener oaol;

    private String name;
    
    private double duration;

    public Animation(String name, List<AnimatedFrame> frames, double duration) {
        this.name = name;
        this.frames = frames;
        currentFrame = 0;
        this.duration = duration;
    }

    public void addFrameRange(String name,int start,int end){
        sub_anims.put(name,new FrameRange(start,end));
    }

    public AnimatedFrame getCurrentFrame() {
        return this.frames.get(currentFrame);
    }

    public double getDuration() {
        return this.duration;        
    }

    public double getDurationNow() {
        return fs_now==null?0:this.duration*fs_now.getFLen()/frames.size();
    }

    public List<AnimatedFrame> getFrames() {
        return frames;
    }

    public String getName() {
        return name;
    }

    public void selectFrames(String name){
        fs_now=sub_anims.get(name);
        currentFrame=fs_now.start;
        name_now=name;
    }

    public AnimatedFrame getNextFrame() {
        nextFrame();
        return this.frames.get(currentFrame);
    }

    public void nextFrame() {
        /*int nextFrame = currentFrame + 1;
        if (nextFrame > frames.size() - 1) {
            currentFrame = 0;
        } else {
            currentFrame = nextFrame;
        }*/
        if(fs_now==null)
            return;

        int nextFrame = currentFrame + 1;
        if (nextFrame > fs_now.end) {
            currentFrame = fs_now.start;
            if(oaol!=null)
                oaol.onOver(name_now);
        } else {
            currentFrame = nextFrame;
        }
    }

    public void setOnAnimationOverListener(OnAnimationOverListener oaol) {
        this.oaol = oaol;
    }

    public class FrameRange{
        public int start;
        public int end;
        public FrameRange(int start,int end){
            this.start=start;
            this.end=end;
        }

        public int getFLen(){
            return end-start;
        }
    }

    public interface OnAnimationOverListener{
        void onOver(String name);
    }

}
