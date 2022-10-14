/*
 * Copyright (c) 2004-2020 The YAWL Foundation. All rights reserved.
 * The YAWL Foundation is a collaboration of individuals and
 * organisations who are committed to improving workflow technology.
 *
 * This file is part of YAWL. YAWL is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation.
 *
 * YAWL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with YAWL. If not, see <http://www.gnu.org/licenses/>.
 */

package org.yawlfoundation.yawl.ui.dynform;


import com.vaadin.flow.component.Component;

import java.util.ArrayList;

/**
 * Maintains a list of components and their combined height
 *
 * Author: Michael Adams
 * Creation Date: 25/02/2008
 */

public class DynFormComponentList extends ArrayList<Component> {

    private int _height;


    public DynFormComponentList() {
        super();
    }
    

    public int getHeight() { return _height; }

    public void setHeight(int height) { _height = height; }


    public void ensureRadioButtonSelection() {
//        RadioButton first = null;
//        for (UIComponent component : this) {
//            if (component instanceof RadioButton) {
//                RadioButton rb = (RadioButton) component;
//                if ((Boolean) rb.getSelected()) {
//                    return;                              // one's selected
//                }
//                if (first == null) first = rb;
//            }
//        }
//
//        // if there's one or more radios, and none is selected, select first
//        if (first != null) first.setSelected(true);
    }

}
