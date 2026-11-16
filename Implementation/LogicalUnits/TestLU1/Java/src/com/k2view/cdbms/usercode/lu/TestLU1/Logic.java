/////////////////////////////////////////////////////////////////////////
// LU Functions
/////////////////////////////////////////////////////////////////////////

package com.k2view.cdbms.usercode.lu.TestLU1;

import com.k2view.cdbms.shared.user.UserCode;
import com.customer.utils.Masker;

public class Logic extends UserCode {

    public static String fnMaskCard(String card) {
        return Masker.mask(card);
    }
}
