package Utile;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author binu
 */

// pour limiter le nombre de caractère pour le téléphone et fax

public class JTextFieldLimit extends PlainDocument {
  private int limit;

  public JTextFieldLimit(int limit) {
   super();
   this.limit = limit;
   }

  public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
    if (str == null) return;

    if ((getLength() + str.length()) <= limit) {
      super.insertString(offset, str, attr);
    }
  }
}
