/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rmj.appdriver.agent;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author kalyptus
 */
public class FixedSizeDoc extends PlainDocument {
   private static final long serialVersionUID = 5017482169716902038L;
   private int maxCharacterSize;

   public FixedSizeDoc( int maxCharacterSize ) {
      super();
      this.maxCharacterSize = maxCharacterSize;
   }

   public void insertString( int offs, String str, AttributeSet a){
      try {
         if( null != str ) {
            if( (this.getLength() + str.length() ) < this.maxCharacterSize ) {
               super.insertString(offs, str, a);
            } else {
               int diff = this.maxCharacterSize - this.getLength();
               //this may happen if they paste info...only allow a portion of it to be inserted upto the max
               if( diff > 0 ) {
                  super.insertString( offs, str.substring(0, diff), a);
               }
            }
         }
      } catch (BadLocationException ex) {
         ex.printStackTrace();
         return;
      }
   }

   public int getMaximumCharacterLength() {
      return this.maxCharacterSize;
   }

   public void setMaxuimumCharacterLength( int size ) {
      this.maxCharacterSize = size;
   }
}
