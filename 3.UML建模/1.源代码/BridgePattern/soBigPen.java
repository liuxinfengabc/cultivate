package BridgePattern;

public class soBigPen extends Pen{
    public void draw(String name) {
        String penType = "超大号毛笔绘制";
        this.color.bepaint(penType, name);
    }
}
