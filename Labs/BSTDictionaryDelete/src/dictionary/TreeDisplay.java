package dictionary;

import java.awt.Dimension; 
import javax.swing.JPanel; 

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class TreeDisplay<T extends Comparable<T>> extends JPanel {

    private int CANVAS_WIDTH = 1000; 

    private int rootY = 10;
    private int NODE_SIZE = 25;
    private int ROW_HEIGHT = 50;
    mxGraph graph = new mxGraph();
    Object parent = graph.getDefaultParent();

    public Object drawTree(WordNode root, int depth, int index) {
        if (root == null) {
            return null;
        }

        int myX = (int) ((CANVAS_WIDTH * (index)) / (Math.pow(2, depth - 1) + 1));

        Object rootVertex = graph.insertVertex(parent, null, root.getWord(), myX, depth * ROW_HEIGHT + rootY, NODE_SIZE,
                NODE_SIZE);

        Object rightChildVertex = drawTree(root.getRight(), depth + 1, index * 2);

        if (rightChildVertex != null) {
            graph.insertEdge(parent, null, "R", rootVertex, rightChildVertex,
                    "startArrow=none;endArrow=none;strokeWidth=1;strokeColor=green");
        }

        Object leftChildVertex = drawTree(root.getLeft(), depth + 1, index * 2 - 1);

        if (leftChildVertex != null) {
            graph.insertEdge(parent, null, "L", rootVertex, leftChildVertex,
                    "startArrow=none;endArrow=none;strokeWidth=1;strokeColor=green");
        }

        return rootVertex;
    }

    /**
     * Redraw the whole tree
     *
     * @param root the root of tree to be drawn
     */
    public void update(WordNode root) {
        graph.getModel().beginUpdate();

        try {
            Object[] cells = graph.getChildCells(parent, true, false);
            graph.removeCells(cells, true);
            drawTree(root, 1, 1);

        } finally {
            graph.getModel().endUpdate();
        }
    }

    public TreeDisplay(WordNode root, int width, int height) {
        this.update(root);

        CANVAS_WIDTH = width;

        // Create the mxGraphComponent
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setMinimumSize(new Dimension(width, height));
        graphComponent.setEnabled(false);

        // Enable mouse wheel zoom
        graphComponent.addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                graphComponent.zoomIn(); // Zoom In
            } else {
                graphComponent.zoomOut(); // Zoom Out
            }
        });

        // Add the graph component to the panel
        this.add(graphComponent);
    }
}
