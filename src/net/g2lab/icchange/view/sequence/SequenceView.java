/**
 * 
 */
package net.g2lab.icchange.view.sequence;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:christoph.kinkeldey@hcu-hamburg.de">Christoph Kinkeldey</a>
 *
 */
public class SequenceView extends JFrame implements ActionListener {

	private static final Log LOG = LogFactory.getLog(Class.class.getName());
	
//	public static final String DIR = "C:/temp/icchange/";
	
//	public static String[] UNCERTAINTY_FILENAMES = { "u-stretch.tif", "u-05-07-int.tif"
//	};
	
	private SequenceListPanel listPanel;
	private JScrollPane scrollPane;

	private JButton filterButton;

	private JComboBox<String> sortBox;
	
	public SequenceView() {
		
		try {
			URL url = ChangeInfoViewTest.class.getResource("es-sub-change.dbf");
//			URL url = new File("C:/temp/icchange/es-sub-change.dbf").toURI().toURL();
		
//			SequenceView view =  SequenceView.createSequenceViewFromCSV(url.openStream());

			Calendar[] dates =
					new Calendar[] { new GregorianCalendar(2010, 5, 20),
					new GregorianCalendar(2010, 8, 6),
					new GregorianCalendar(2010, 10, 9)};
			String[] objClassAttr = {"Klassen", "Klassen_1", "Klassen_12"};
			String[] classcodeAttr = {"classcode", "classcode_", "classcode1"};
			String[] uncertaintyAttr = {"u", "u_1", "u_12"};
			this.listPanel =  SequenceListPanel.createSequenceViewFromDBF(url.openStream(), dates, objClassAttr, classcodeAttr, uncertaintyAttr);

//			List<ChangeSequenceGroup> sequenceGroups = new ArrayList<ChangeSequenceGroup>();
//			
//			ObjectClass[] objClasses = new ObjectClass[3];
//			objClasses[0] = new ObjectClass("0");
//			objClasses[1] = new ObjectClass("1");
//			objClasses[2] = new ObjectClass("2");
//			
//			Calendar[] dates = new Calendar[] { new GregorianCalendar(2010, 5, 20),
//					new GregorianCalendar(2010, 8, 6),
//					new GregorianCalendar(2010, 10, 9)};
//			
//			URL changeURL = new File("c:/temp/icchange/all-change1.tif").toURI().toURL();
//			GridCoverage2D change = new RasterIO().readCoverage(changeURL);
//			RenderableImage changeImage = change.getRenderableImage(0, 1);
//			Raster changeData = changeImage.createDefaultRendering().getData();
//			
//			URL uncertaintyURL0 = new File(DIR + UNCERTAINTY_FILENAMES[0]).toURI().toURL();
//			GridCoverage2D uncertainty0 = new RasterIO().readCoverage(uncertaintyURL0);
//			RenderableImage uncertImage0 = uncertainty0.getRenderableImage(0, 1);
//			Raster uncertData0 = uncertImage0.createDefaultRendering().getData();
//			
//			URL uncertaintyURL1 = new File(DIR + UNCERTAINTY_FILENAMES[1]).toURI().toURL();
//			GridCoverage2D uncertainty1 = new RasterIO().readCoverage(uncertaintyURL1);
//			RenderableImage uncertImage1 = uncertainty1.getRenderableImage(0, 1);
//			Raster uncertData1 = uncertImage1.createDefaultRendering().getData();
//
//			ChangeSequenceGroup sequenceGroup = new ChangeSequenceGroup();
//			int[] changePixelArray = new int[1];
//			int[] uncert0PixelArray = new int[1];
//			int[] uncert1PixelArray = new int[1];
//			for (int i = 0; i < changeData.getHeight(); i++) {
//				for (int j = 0; j < changeData.getWidth(); j++) {
//					changeData.getPixel(i, j, changePixelArray);
//					uncertData0.getPixel(i, j, uncert0PixelArray);
//					uncertData1.getPixel(i, j, uncert1PixelArray);
//					if (changePixelArray[0] == 1) {
//						ChangeSequence sequence = new ChangeSequence();
//						sequence.setClasses(objClasses);
//						sequence.setDates(dates);
////						sequence.setNumber(1);
//						sequence.setUncertainties(new double[] {uncert0PixelArray[0], uncert1PixelArray[0]});
//						sequenceGroup.add(sequence);
//					}
//				}
//			}
//			
//			sequenceGroups.add(sequenceGroup);
//			this.listPanel = new SequenceListPanel(sequenceGroups, objClasses);
			
			this.scrollPane = new JScrollPane(listPanel);
			this.add(scrollPane, BorderLayout.CENTER);
			
			this.filterButton = new JButton("filter");
			filterButton.addActionListener(this);
			add(filterButton, BorderLayout.SOUTH);
			
			String[] elements = {"SORT BY AMOUNT", "SORT BY UNCERTAINTY",
					"SORT BY OBJCLASS Date 1", "SORT BY OBJCLASS Date 2", "SORT BY OBJCLASS Date 3"};
			this.sortBox = new JComboBox<String>(elements);
			sortBox.addActionListener(this);
			add(sortBox, BorderLayout.NORTH);
			
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.pack();
			
		} catch (IOException e) {
			LOG.error("Cannot load: " + e, e);
		} catch (ParseException e) {
			LOG.error(e, e);
		} 
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(filterButton)) {
			LOG.info("filter");
			listPanel.filterChangeSequenceGroups();
			listPanel.updateList();
			listPanel.repaint();
		} else if (e.getSource().equals(sortBox)) {
			LOG.info("sort");
			listPanel.setSortBy(SequenceListPanel.SORT_BY[sortBox.getSelectedIndex()]);
			listPanel.updateList();
			listPanel.repaint();
		}
	}
	
}
