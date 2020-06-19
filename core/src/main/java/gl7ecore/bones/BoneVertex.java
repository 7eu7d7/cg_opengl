package gl7ecore.bones;

import java.util.LinkedList;

public class BoneVertex {
    //一个顶点所受的骨骼影响集合
    public LinkedList<Integer> bid=new LinkedList<Integer>();
    public LinkedList<Float> weight=new LinkedList<Float>();

    public void addBoneData(Integer bid, float weight)
    {
        if(this.bid.size()>=4)
            return;
        this.bid.offer(bid);
        this.weight.offer(weight);
    }
}
