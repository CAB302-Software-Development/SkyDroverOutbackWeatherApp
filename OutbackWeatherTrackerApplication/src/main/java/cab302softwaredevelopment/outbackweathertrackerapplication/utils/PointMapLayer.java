package cab302softwaredevelopment.outbackweathertrackerapplication.utils;

import com.gluonhq.maps.MapLayer;
import com.gluonhq.maps.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class PointMapLayer extends MapLayer {
    private List<Pair<MapPoint, Node>> points;

    public PointMapLayer() {
        this.points = new ArrayList<>();
    }

    public PointMapLayer(List<Pair<MapPoint, Node>> points) {
        this.points = points;
    }

    public void addPoint(MapPoint point) {
        addPoint(point, new Circle(5, Color.RED));
    }

    public void addPoint(MapPoint point, Node node) {
        points.add(new Pair<>(point, node));
        this.markDirty();
    }

    @Override
    protected void layoutLayer() {
        this.getChildren().clear();
        for (Pair<MapPoint, Node> candidate : points) {
            MapPoint point = candidate.getKey();
            Node icon = candidate.getValue();
            Point2D mapPoint = getMapPoint(point.getLatitude(), point.getLongitude());
            icon.setTranslateX(mapPoint.getX());
            icon.setTranslateY(mapPoint.getY());
            icon.setVisible(true);
            this.getChildren().add(icon);
        }
    }
}
