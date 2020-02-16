package BridgePattern;

public class MiddlePen extends Pen {
    public void draw(String name) {
        String penType = "中号毛笔绘制";
        this.color.bepaint(penType, name);
    }
}