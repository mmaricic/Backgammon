package etf.backgammon.mm140457d.GUI;

import javax.swing.JApplet;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedOrderedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.subLayout.TreeCollapser;
import etf.backgammon.mm140457d.players.Node;
import etf.backgammon.mm140457d.players.Type;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

public class GraphicTree extends JApplet {
	GameScreen gameScreen;
	int expandedLevel = 0;
	TreeLayout<Node, Branch> layout;

	public GraphicTree(Node root, GameScreen gameScreen) {

		this.gameScreen = gameScreen;

		// graf i layout
		DelegateForest<Node, Branch> tree = new DelegateForest<Node, Branch>(
				new DirectedOrderedSparseMultigraph<Node, Branch>());
		gameScreen.showGraphicTree(tree, root);
		layout = new TreeLayout<Node, Branch>(tree, 60, 100);

		// visualization viewer
		VisualizationViewer<Node, Branch> vv = new VisualizationViewer<Node, Branch>(layout, new Dimension(1000, 600));
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

		final Color vertexLabelColor = Color.WHITE;
		DefaultVertexLabelRenderer vertexLabelRenderer = new DefaultVertexLabelRenderer(vertexLabelColor) {
			@Override
			public <Node> Component getVertexLabelRendererComponent(JComponent vv, Object value, Font font,
					boolean isSelected, Node vertex) {
				super.getVertexLabelRendererComponent(vv, value, font, isSelected, vertex);
				setForeground(vertexLabelColor);
				setFont(new Font("Times new Roman", Font.BOLD, 12));
				return this;
			}
		};
		vv.getRenderContext().setVertexLabelRenderer(vertexLabelRenderer);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller() {
			@Override
			public String transform(Object v) {
				if (v instanceof Graph)
					return "";
				return super.transform(v);
			}
		});

		Container contentPane = getContentPane();

		// scrollPane
		final GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(vv);
		contentPane.add(scrollPane);
		scrollPane.setBackground(new Color(245, 222, 179));
		TreeCollapser collapser;
		collapser = new TreeCollapser();

		final DefaultModalGraphMouse<Node, Branch> graphMouse = new DefaultModalGraphMouse<Node, Branch>();
		vv.setGraphMouse(graphMouse);
		JComboBox modeBox = graphMouse.getModeComboBox();// da li treba
		modeBox.addItemListener(graphMouse.getModeListener());// da li treba
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		// modeBox.setVisible(false);

		// velicina cvorova
		Transformer<Node, Shape> vertexSize = new Transformer<Node, Shape>() {
			public Shape transform(Node i) {
				if(i.getType() == Type.CHANCE || i.getType() == Type.TERMINAL){
					Rectangle2D square = new Rectangle(-15, -15, 40, 30);
					return square;
				}
				else{
					Ellipse2D circle = new Ellipse2D.Double(-15, -15, 35, 35);
				// in this case, the vertex is twice as large
				return circle;
				}
			}
		};
		vv.getRenderContext().setVertexShapeTransformer(vertexSize);

		
		//boja cvorova
		Transformer<Node, Paint> vertexColor = new Transformer<Node, Paint>() {
			public Paint transform(Node i) {
				if(vv.getPickedVertexState().getPicked().contains(i))
					return new Color(245,215,120);
				return new Color(153, 51, 0);
			}
		};
		vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
		vv.setVertexToolTipTransformer(new ToStringLabeller());
		vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));

		JButton expand = new JButton("Expand");
		expand.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Collection picked = vv.getPickedVertexState().getPicked();

				for (Object v : picked) {
					if (v instanceof Node) {
						gameScreen.showGraphicTree(tree, (Node) v);
						layout = new TreeLayout<Node, Branch>(tree, 60, 90);
						vv.setGraphLayout(layout);
					}
					if (v instanceof Forest) {
						Forest inForest = (Forest) layout.getGraph();
						collapser.expand(inForest, (Forest) v);

					}

					vv.getPickedVertexState().clear();
					vv.repaint();
				}
			}
		});

		JButton collapse = new JButton("Collapse");
		collapse.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Collection picked = new HashSet(vv.getPickedVertexState().getPicked());
				if (picked.size() == 1) {
					Object newRoot = picked.iterator().next();
					Forest inTree = (Forest) layout.getGraph();

					try {
						collapser.collapse(vv.getGraphLayout(), inTree, newRoot);
					} catch (InstantiationException e1) {
					} catch (IllegalAccessException e1) {
					}

					vv.getPickedVertexState().clear();
					vv.repaint();
				}
			}
		});

		JButton showState = new JButton("Show game state");
		showState.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Collection picked = vv.getPickedVertexState().getPicked();
				for (Object v : picked) {
					if (v instanceof Node) {
						((Node) v).setText();
					}
					vv.getPickedVertexState().clear();
					vv.repaint();
				}
			}
		});

		JButton hideState = new JButton("Hide game state");
		hideState.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Collection picked = vv.getPickedVertexState().getPicked();
				for (Object v : picked) {
					if (v instanceof Node) {
						((Node) v).hideText();
					}
					vv.getPickedVertexState().clear();
					vv.repaint();
				}
			}
		});

		final ScalingControl scaler = new CrossoverScalingControl();
		vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());
			}
		});

		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1 / 1.1f, vv.getCenter());
			}
		});

		JPanel scaleGrid = new JPanel(new GridLayout(1, 0));
		scaleGrid.setBorder(BorderFactory.createTitledBorder("Zooming"));
		scaleGrid.add(plus);
		scaleGrid.add(minus);

		JPanel controls = new JPanel();
		controls.add(scaleGrid);
		controls.add(expand);
		controls.add(collapse);
		controls.add(showState);
		controls.add(hideState);
		controls.add(modeBox);
		contentPane.add(controls, BorderLayout.SOUTH);
		contentPane.setBackground(new Color(245, 222, 179));
		controls.setBackground(new Color(153, 51, 0));
		vv.setBackground(new Color(240, 200, 147));
		vv.repaint();
	}

}