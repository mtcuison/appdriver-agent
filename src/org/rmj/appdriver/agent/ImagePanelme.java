/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rmj.appdriver.agent;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanelme extends JPanel {
   private static final long serialVersionUID = -7287334140890820961L;
   private Image img;

   /**
    * @wbp.parser.constructor
    */
   public ImagePanelme(String img) {
      this(new ImageIcon(img).getImage());
   }

   public ImagePanelme(Image img) {
      this.img = img;

      Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
      setPreferredSize(size);
      setMinimumSize(size);
      setMaximumSize(size);
      setSize(size);
      setLayout(null);
   }

   public void paintComponent(Graphics g) {
      g.drawImage(img, 0, 0, null);
   }
}

