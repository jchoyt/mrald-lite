/*
 *  Copyright 2008 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mitre.mrald.util;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.print.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;


public class PrintPreview extends JFrame {

	protected int pageWidth;
	protected int pageHeight;
	protected JComboBox scaleCombo;

	protected PreviewContainer preview;

	public PrintPreview(Component[] targets) {

		this(targets, "Print Preview");
	}

	public PrintPreview(Component[] targets, String title)
	{

		super(title);
		//setDefaultLookAndFeelDecorated( true );

		preview = new PreviewContainer();

		init(targets);
		getPreviews(targets);

		JScrollPane ps = new JScrollPane(preview);
		getContentPane().add(ps, BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		ActionListener lst = getPreviewActionListener();

		scaleCombo.addActionListener(lst);
		scaleCombo.setMaximumSize(scaleCombo.getPreferredSize());
		scaleCombo.setEditable(true);

		scaleCombo.setSelectedIndex(3);
	}


	private void getPreviews(Component[] targets)
	{
		PrintUtilities printUtil = new PrintUtilities(targets);
		int pageIndex =0;
		int size = targets.length;
		int scale = 50;

		int w = (pageWidth * scale / 100);
		int h = (pageHeight * scale / 100);

		BufferedImage graphCache;
		Graphics g;
		PrinterJob prnJob = PrinterJob.getPrinterJob();
		PageFormat pageFormat = prnJob.defaultPage();
		if (pageFormat.getHeight() == 0 || pageFormat.getWidth() == 0)
		{
			System.err.println("Unable to determine default page size");
			System.out.println("Unable to determine default page size");
			return;
		}

		pageWidth = (int) (pageFormat.getWidth());
		pageHeight = (int) (pageFormat.getHeight());
		for (int i=0; i < size; i++)
		{
			pageIndex=i;
			Graphics2D g2 =  (Graphics2D) targets[i].getGraphics();

			if (g2 != null)
			{
				System.out.println("Got G2 Graphics");

				 graphCache = g2.getDeviceConfiguration().createCompatibleImage(pageWidth, (pageHeight));
				 g= graphCache.getGraphics();

				 g.setColor(Color.white);
				 g.fillRect(0, 0, pageWidth, pageHeight);

				 if (printUtil.print(g, pageFormat, pageIndex) !=Printable.PAGE_EXISTS)
					break;

				 PagePreview pp = new PagePreview(w, h, graphCache);
				 preview.add(pp);
			}
			else
			{
//				Rectangle rect = getBounds();
				System.out.println("About to PrintPreview Graphics");

				PagePreview pp = new PagePreview(w, h, ((ImageDisplayPanel)targets[i]).getCurrentImage() );

				g = preview.getGraphics();
				if (g==null)
					System.out.print("Graphics object is null");
				preview.add(pp);
			}

			pageIndex++;
		}


	}

	/* PM: Never used!
	private void getPreviews(Component[] targets, Image[] images)
	{
		PrintUtilities printUtil = new PrintUtilities(targets);
		int pageIndex =0;
		int size = targets.length;
		int scale = 50;

		int w = (int) (pageWidth * scale / 100);
		int h = (int) (pageHeight * scale / 100);


		PrinterJob prnJob = PrinterJob.getPrinterJob();
		PageFormat pageFormat = prnJob.defaultPage();
		if (pageFormat.getHeight() == 0 || pageFormat.getWidth() == 0)
		{
			System.err.println("Unable to determine default page size");
			return;
		}

		pageWidth = (int) (pageFormat.getWidth());
		pageHeight = (int) (pageFormat.getHeight());
		for (int i=0; i < size; i++)
		{
			pageIndex=i;
			Graphics g = images[i].getGraphics();
			if (printUtil.print(g, pageFormat, pageIndex) !=Printable.PAGE_EXISTS)
				break;

			PagePreview pp = new PagePreview(w, h, images[i]);
			preview.add(pp);

			pageIndex++;
		}


	}*/


	private ActionListener initListener(Component[] targets)
	{
		final Component[]  printComponents = targets;
		ActionListener lst = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					PrintUtilities.printComponent(printComponents);
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					dispose();

			}

		};

		return lst;
	}

	private JToolBar initToolBar(Component[] targets)
	{

			setSize(800, 600);
			JToolBar tb = new JToolBar();
//			Image printImage;
			URL imageURL=null;
			try
			{
				imageURL = new URL("http://127.0.0.1:8080/graphics/print.gif") ;
//				printImage = java.awt.Toolkit.getDefaultToolkit().createImage(imageURL);
			}
			catch(MalformedURLException m)
			{}

			//JButton bt = new JButton("Print", new ImageIcon("print.gif"));
			JButton bt = new JButton("Print", new ImageIcon(imageURL));

			ActionListener lst = initListener(targets);
			bt.addActionListener(lst);
			bt.setAlignmentY(0.5f);
			bt.setMargin(new Insets(4, 6, 4, 6));

			tb.add(bt);

			bt = new JButton("Close");

			lst = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			};

			bt.addActionListener(lst);
			bt.setAlignmentY(0.5f);
			bt.setMargin(new Insets(2, 6, 2, 6));
			tb.add(bt);
			return tb;
	}

	private void init(Component[] targets)
	{
		setSize(800, 600);
		JToolBar tb =initToolBar(targets);
		String[] scales = { "10 %", "25 %", "50 %", "100 %",  "150 %",  "200 %" };

		scaleCombo = new JComboBox(scales);

		tb.addSeparator();
		tb.add(scaleCombo);
		scaleCombo.setSelectedIndex(3);

		getContentPane().add(tb, BorderLayout.NORTH);
	}

	private ActionListener getPreviewActionListener()
	{
		ActionListener lst = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Thread runner = new Thread() {

					public void run() {

						String str = scaleCombo.getSelectedItem().toString();

						if (str.endsWith("%"))
							str = str.substring(0, str.length() - 1);

						str = str.trim();
						int scale = 0;

						try
						{
							scale = Integer.parseInt(str);
						}
						catch (NumberFormatException ex)
						{
							return;
						}

						int w = (pageWidth * scale / 100);
						int h = (pageHeight * scale / 100);

						Component[] comps = preview.getComponents();

						for (int k = 0; k < comps.length; k++)
						{

							if (!(comps[k] instanceof PagePreview))
								continue;

							PagePreview pp = (PagePreview) comps[k];
							pp.setScaledSize(w, h);
						}

						preview.doLayout();
						//preview.getParent().getParent().validate();
					}
				};

				runner.start();
			}
		};
		return lst;
	}

	class PreviewContainer extends JPanel
	{
		protected int H_GAP = 16;
		protected int V_GAP = 10;

		public Dimension getPreferredSize()
		{

			int n = getComponentCount();
			if (n == 0)
				return new Dimension(H_GAP, V_GAP);

			Component comp = getComponent(0);
			Dimension dc = comp.getPreferredSize();

			int w = dc.width;
			int h = dc.height;

			Dimension dp = getParent().getSize();

			int nCol = Math.max((dp.width - H_GAP) / (w + H_GAP), 1);
			int nRow = n / nCol;

			if (nRow * nCol < n)
				nRow++;

			int ww = nCol * (w + H_GAP) + H_GAP;
			int hh = nRow * (h + V_GAP) + V_GAP;

			Insets ins = getInsets();

			return new Dimension(ww + ins.left + ins.right,
			hh + ins.top + ins.bottom);
		}

		public Dimension getMaximumSize() {

			return getPreferredSize();
		}

		public Dimension getMinimumSize() {

			return getPreferredSize();
		}

		public void doLayout() {

			Insets ins = getInsets();

			int x = ins.left + H_GAP;
			int y = ins.top + V_GAP;
			int n = getComponentCount();

			if (n == 0)
				return;

			Component comp = getComponent(0);
			Dimension dc = comp.getPreferredSize();

			int w = dc.width;
			int h = dc.height;

			Dimension dp = getParent().getSize();

			int nCol = Math.max((dp.width - H_GAP) / (w + H_GAP), 1);

			int nRow = n / nCol;
			if (nRow * nCol < n)
				nRow++;

			int index = 0;

			for (int k = 0; k < nRow; k++) {
				for (int m = 0; m < nCol; m++) {

					if (index >= n)
						return;

					comp = getComponent(index++);
					comp.setBounds(x, y, w, h);
					x += w + H_GAP;

				}

				y += h + V_GAP;
				x = ins.left + H_GAP;
			}
		}
	}

	class PagePreview extends JPanel
	{
		protected int m_w;
		protected int m_h;
		protected Image m_source;

		public PagePreview(int w, int h, Image source)
		{
			m_w = w;
			m_h = h;
			m_source = source;
			setBackground(Color.white);
			//setBorder(new MatteBorder(1, 1, 2, 2, Color.black));
		}

		public void setScaledSize(int w, int h)
		{
			m_w = w;
			m_h = h;
			repaint();
		}

		public Dimension getPreferredSize()
		{
			Insets ins = getInsets();
			return new Dimension(m_w + ins.left + ins.right, m_h + ins.top + ins.bottom);
		}

		public Dimension getMaximumSize()
		{
			return getPreferredSize();
		}

		public Dimension getMinimumSize()
		{
			return getPreferredSize();
		}

		public void paint(Graphics g)
		{
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(m_source, 0, 0, (m_w), (m_h), this);
			paintBorder(g);

		}

	}
}