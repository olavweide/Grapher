package no.uib.ii.algo.st8.util;

import no.uib.ii.algo.st8.model.DefaultEdge;
import no.uib.ii.algo.st8.model.DefaultVertex;

import org.jgrapht.graph.SimpleGraph;

import android.graphics.Matrix;

public class GraphExporter {

	// TODO zero-pad
	// int red = Color.red(c.getColor());
	// int green = Color.green(c.getColor());
	// int blue = Color.blue(c.getColor());

	public static String getMetapost(
			SimpleGraph<DefaultVertex, DefaultEdge<DefaultVertex>> graph) {
		String res = "";

		res += "%Metapostified\n\n";
		res += "pair n[];\n";
		res += "numeric skalering;\n\n";
		res += "skalering := 0.3;";
		res += "vardef drawvertex(expr i) =\n";
		res += "    dotlabel.urt(decimal i,skalering*n[i]);\n";
		res += "enddef;\n\n";

		res += "vardef drawedge (expr inn,ut) =\n";
		res += "    draw (skalering*inn) -- (skalering*ut);\n";
		res += "enddef;\n\n";

		for (DefaultVertex v : graph.vertexSet()) {
			res += "n" + v.getId() + " = (" + v.getCoordinate().getX() + ","
					+ v.getCoordinate().getY() + ");\n";
		}
		res += "\n";
		for (DefaultEdge<DefaultVertex> e : graph.edgeSet()) {
			res += "drawedge(n" + e.getSource().getId() + ", n"
					+ e.getTarget().getId() + ");\n";
		}

		res += "for i = 1 step 1 until 11:\n";
		res += "    drawvertex(i);\n";
		res += "endfor;\n";

		return res;

	}

	private static String getBeginFigure() {
		String res = "";
		res += "\\begin{figure}\n\t\\centering";
		return res;
	}

	private static String getEndFigure(
			SimpleGraph<DefaultVertex, DefaultEdge<DefaultVertex>> graph) {
		// String info = graph.graphInfo();
		String info = "";
		String infop = info.replaceAll(" ", "-").replaceAll(",", "-")
				.replaceAll("\\.", "-").toLowerCase();
		String res = "";
		res += "\n\t\\caption{" + info + "}\n";
		res += "\t\\label{fig:" + infop + "}\n";
		res += "\\end{figure}\n";
		return res;
	}

	public static String getTikz(
			SimpleGraph<DefaultVertex, DefaultEdge<DefaultVertex>> graph,
			Matrix transformation) {
		String res = "\n\t\\begin{tikzpicture}";
		res += "[every node/.style={circle, draw, scale=.6}, scale=1.0, rotate = 180, xscale = -1]\n\n";

		for (DefaultVertex v : graph.vertexSet()) {
			float[] points = { v.getCoordinate().getX(),
					v.getCoordinate().getY() };
			transformation.mapPoints(points);

			float x = Math.round(points[0]) / 100f;
			float y = Math.round(points[1]) / 100f;

			res += "\t\t\\node (" + v.getId() + ") at";
			res += " ( " + x + ", " + y + ")";
			res += " {};\n";
		}
		res += "\n";
		for (DefaultEdge<DefaultVertex> e : graph.edgeSet()) {
			res += "\t\t\\draw (" + e.getSource().getId() + ") -- ("
					+ e.getTarget().getId() + ");\n";
		}
		res += "\n";
		res += "\t\\end{tikzpicture}";

		return getBeginFigure() + res + getEndFigure(graph);

	}
}
