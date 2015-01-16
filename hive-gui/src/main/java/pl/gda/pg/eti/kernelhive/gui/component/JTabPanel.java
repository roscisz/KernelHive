/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Marcel Schally-Kacprzak
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.gui.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

public class JTabPanel extends JPanel {

	private static final long serialVersionUID = 8529416373302749016L;
	private static final Logger LOG = Logger.getLogger(JTabPanel.class
			.getName());

	private final JTabbedPane pane;
	private JTabContent content;
	private final JLabel label;

	public JTabPanel(final JTabContent tabPanel) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.content = tabPanel;
		this.content.setTabPanel(this);
		if (this.content.getParent() != null
				&& this.content.getParent() instanceof JTabbedPane) {
			this.pane = (JTabbedPane) this.content.getParent();
		} else {
			LOG.severe("KH: Tabbed Pane is not a parent or parent is null!");
			throw new NullPointerException("Tabbed Pane is null!");
		}
		setOpaque(false);
		label = new JLabel() {
			private static final long serialVersionUID = -5214456099552542411L;

			@Override
			public String getText() {
				final int i = pane.indexOfTabComponent(JTabPanel.this);
				if (i != -1) {
					return pane.getTitleAt(i);
				}
				return null;
			}

			@Override
			public void setText(final String text) {
				final int i = pane.indexOfTabComponent(JTabPanel.this);
				if (i != -1) {
					pane.setTitleAt(i, text);
				}
			}
		};

		add(label);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

		final JButton tabButton = new TabButton();
		add(tabButton);
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	private final static MouseListener buttonMouseListener = new MouseAdapter() {

		@Override
		public void mouseEntered(final MouseEvent e) {
			final Component c = e.getComponent();
			if (c instanceof AbstractButton) {
				final AbstractButton ab = (AbstractButton) c;
				ab.setBorderPainted(true);
			}
		}

		@Override
		public void mouseExited(final MouseEvent e) {
			final Component c = e.getComponent();
			if (c instanceof AbstractButton) {
				final AbstractButton ab = (AbstractButton) c;
				ab.setBorderPainted(false);
			}
		}
	};

	private class TabButton extends JButton implements ActionListener {

		private static final long serialVersionUID = 1111747043053025898L;

		public TabButton() {
			final int size = 17;
			setPreferredSize(new Dimension(size, size));
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			addActionListener(this);
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			final int i = pane.indexOfComponent(content);
			if (i != -1) {
				content.getFrame().getController().closeTab(JTabPanel.this);
			}
		}

		@Override
		public void updateUI() {
		}

		@Override
		protected void paintComponent(final Graphics g) {
			super.paintComponents(g);
			final Graphics2D g2 = (Graphics2D) g.create();
			if (getModel().isPressed()) {
				g2.translate(1, 1);
			}
			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);
			final int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight()
					- delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight()
					- delta - 1);
			g2.dispose();
		}

	}

	/**
	 * gets the label of this tab panel
	 * 
	 * @return JLabel
	 */
	public JLabel getLabel() {
		return label;
	}

	/**
	 * gets the tab content corresponding with this tab panel;
	 * 
	 * @return JTabConent
	 */
	public JTabContent getTabContent() {
		return content;
	}

	/**
	 * sets the tab content
	 * 
	 * @param content
	 *            JTabContent
	 */
	public void setTabContent(final JTabContent content) {
		this.content = content;
	}

	/**
	 * gets the tab pane parent of this component
	 * 
	 * @return
	 */
	public JTabbedPane getPane() {
		return pane;
	}
}
