package cab302softwaredevelopment.outbackweathertrackerapplication.models;

import com.gluonhq.maps.MapPoint;
import javafx.scene.Node;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectablePoint {
    private final MapPoint point;
    private final Node selectedStyle;
    private final Node unselectedStyle;
    private final Object data;

    public SelectablePoint(MapPoint point, Node selectedStyle, Node unselectedStyle, Object data) {
        this.point = point;
        this.selectedStyle = selectedStyle;
        this.unselectedStyle = unselectedStyle;
        this.data = data;
    }

    public SelectablePoint(MapPoint point, Node selectedStyle, Node unselectedStyle) {
        this.point = point;
        this.selectedStyle = selectedStyle;
        this.unselectedStyle = unselectedStyle;
        this.data = null;
    }
}
