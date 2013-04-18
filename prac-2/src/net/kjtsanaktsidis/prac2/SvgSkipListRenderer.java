package net.kjtsanaktsidis.prac2;

import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.SortedSet;

public class SvgSkipListRenderer {

    private static final int dx = 40;

    public static void SkipListToSVG(SkipListDict sl, OutputStreamWriter textOut) {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();

        String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
        Document doc = impl.createDocument(svgNS, "svg", null);
        Element svgRoot = doc.getDocumentElement();

        SkipListDict.Tower curT = sl.leftSentinel;
        int rightTpos = sl.size();
        int position = 0;
        while (curT != sl.rightSentinel) {
            String keyS = "";
            if (!curT.isSentinel)
                keyS = curT.key.toString();
            ArrayList<Integer> lws = new ArrayList<>();
            for (int i = 0; i < curT.flinks.length; i++) {
                //count the number of steps back
                SkipListDict.Tower revT = curT.flinks[i];
                int width = 0;
                while (revT != curT) {
                    revT = revT.blinks[0];
                    width++;
                }
                lws.add(width);
            }

            Element gEl = addTower(doc, keyS, position, lws, curT.searchPathFollowed + 1);
            svgRoot.appendChild(gEl);
            position++;
            curT = curT.flinks[0];
        }
        //Draw the right sentinal tower
        svgRoot.appendChild(rSentTower(doc, position, sl.rightSentinel.blinks.length));

        try {
            DOMUtilities.writeDocument(doc, textOut);
        }
        catch (IOException e) {

        }
    }

    private static Element rSentTower(Document doc, int position, int nboxes) {
        //create our group
        Element gEl = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "g");
        //it should be transformed right by dx*position pixels
        gEl.setAttributeNS(null, "transform", "scale(1,-1)translate(" + 2*dx * position + ", -400)");

        //now tack on a rectangle for the key
        Element keyBox = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "rect");
        keyBox.setAttributeNS(null, "width", "40");
        keyBox.setAttributeNS(null, "height", "40");
        keyBox.setAttributeNS(null, "x", "0");
        keyBox.setAttributeNS(null, "y", "0");
        keyBox.setAttributeNS(null, "stroke-width", "3");
        keyBox.setAttributeNS(null, "stroke", "rgb(0, 0, 0)");
        keyBox.setAttributeNS(null, "fill", "rgb(184, 112, 77)");
        gEl.appendChild(keyBox);

        for (int i = 0; i < nboxes; i++) {
            Element linkBox = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "rect");
            linkBox.setAttributeNS(null, "width", "40");
            linkBox.setAttributeNS(null, "height", "40");
            linkBox.setAttributeNS(null, "y", ((Integer)((i + 1)*40)).toString());
            linkBox.setAttributeNS(null, "x", "0");
            linkBox.setAttributeNS(null, "stroke-width", "3");
            linkBox.setAttributeNS(null, "stroke", "rgb(0, 0, 0)");
            linkBox.setAttributeNS(null, "fill", "rgb(220, 184, 166)");
            gEl.appendChild(linkBox);
        }

        return gEl;
    }

    private static Element addTower(Document doc, String key, int position, ArrayList<Integer> lLinkWidths,
                                    int sPathFollowed) {
        //create our group
        Element gEl = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "g");
        //it should be transformed right by dx*position pixels
        gEl.setAttributeNS(null, "transform", "scale(1,-1)translate(" + 2*dx * position + ", -400)");

        //now tack on a rectangle for the key
        Element keyBox = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "rect");
        keyBox.setAttributeNS(null, "width", "40");
        keyBox.setAttributeNS(null, "height", "40");
        keyBox.setAttributeNS(null, "x", "0");
        keyBox.setAttributeNS(null, "y", "0");
        keyBox.setAttributeNS(null, "stroke-width", "3");
        keyBox.setAttributeNS(null, "stroke", "rgb(0, 0, 0)");
        keyBox.setAttributeNS(null, "fill", "rgb(184, 112, 77)");
        Element keyText = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "text");
        keyText.setAttributeNS(null, "font-size", "24");
        keyText.setAttributeNS(null, "x", "10");
        keyText.setAttributeNS(null, "y", "-8");
        keyText.setAttributeNS(null, "transform", "scale(1,-1)");
        keyText.setTextContent(key);

        gEl.appendChild(keyBox);
        gEl.appendChild(keyText);

        //and we should create a box for each tower
        int heightct = 1;
        for (Integer w : lLinkWidths) {
            Element linkBox = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "rect");
            linkBox.setAttributeNS(null, "width", "40");
            linkBox.setAttributeNS(null, "height", "40");
            linkBox.setAttributeNS(null, "y", ((Integer)(heightct*40)).toString());
            linkBox.setAttributeNS(null, "x", "0");
            linkBox.setAttributeNS(null, "stroke-width", "3");
            linkBox.setAttributeNS(null, "stroke", "rgb(0, 0, 0)");
            linkBox.setAttributeNS(null, "fill", "rgb(220, 184, 166)");

            //and draw the link
            Element linkLine = doc.createElementNS(SVGDOMImplementation.SVG_NAMESPACE_URI, "line");
            linkLine.setAttributeNS(null, "x1", "20");
            linkLine.setAttributeNS(null, "x2", ((Integer)(20 + w * 80)).toString());
            linkLine.setAttributeNS(null, "y1", ((Integer) (heightct * 40 + 20)).toString());
            linkLine.setAttributeNS(null, "y2", ((Integer)(heightct*40 + 20)).toString());
            linkLine.setAttributeNS(null, "stroke-width", "3");
            if (heightct == sPathFollowed)
                linkLine.setAttributeNS(null, "stroke", "rgb(255, 0, 0)");
            else
                linkLine.setAttributeNS(null, "stroke", "rgb(0, 0, 255)");
            heightct++;

            gEl.appendChild(linkBox);
            gEl.appendChild(linkLine);
        }

        return gEl;

    }

    public static void main(String[] args) {
        SkipListDict<Integer, String> dict = new SkipListDict<>();
        dict.put(8, "eight");
        dict.put(4, "four");
        dict.put(1, "one");
        dict.put(9, "nine");
        dict.put(5, "five");
        dict.put(0, "zero");
        dict.put(14, "fourteen");

        try {
            OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream("C:\\Users\\KJ\\Dropbox\\otest.svg"));
            SkipListToSVG(dict, w);
            w.close();
        }
        catch (IOException e) {
            System.out.println("IOException");
        }

    }
}
