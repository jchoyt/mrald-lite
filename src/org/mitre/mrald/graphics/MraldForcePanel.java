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
package org.mitre.mrald.graphics;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import edu.berkeley.guir.prefusex.force.ForcePanel;
import edu.berkeley.guir.prefusex.force.ForceSimulator;
/**
 * A Swing user interface component for configuring the parametes of the
 * Force functions in the given ForceSimulator. Useful for exploring
 * different parameterizations when crafting a visualization.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class MraldForcePanel extends ForcePanel {

//    private ForceConstantAction action = new ForceConstantAction();
    private ForceFreezeAction freezeAction = new ForceFreezeAction();
//    private ForceSimulator fsim;

    public MraldForcePanel(ForceSimulator fsim) {
	    super(fsim);
	    addButton();
    } //

    private void addButton() {

	JButton jbut = new JButton("Freeze");
	jbut.addActionListener(freezeAction);
	this.add(jbut);
    } //




    /* Never used!
    private void initUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Force[] forces = fsim.getForces();
        for ( int i=0; i<forces.length; i++ ) {
            Force f = forces[i];
            Box v = new Box(BoxLayout.Y_AXIS);
            for ( int j=0; j<f.getParameterCount(); j++ ) {
                v.add(createField(f,j));
            }
            String name = f.getClass().getName();
            name = name.substring(name.lastIndexOf(".")+1);
            v.setBorder(BorderFactory.createTitledBorder(name));
            this.add(v);
        }
	/***	Add a Freeze Button**** /
	addButton();

        this.add(Box.createVerticalGlue());
    } //*/

    /* Never used!
    private Box createField(Force f, int param) {
        Box h = new Box(BoxLayout.X_AXIS);

        float curVal = f.getParameter(param);

        JLabel     label = new JLabel(f.getParameterName(param));
        label.setPreferredSize(new Dimension(100,20));
        label.setMaximumSize(new Dimension(100,20));

        JTextField text  = new JTextField(
                String.valueOf(curVal));
        text.setPreferredSize(new Dimension(200,20));
        text.setMaximumSize(new Dimension(200,20));
        text.putClientProperty("force", f);
        text.putClientProperty("param", new Integer(param));
        text.addActionListener(action);
        h.add(label);
        h.add(Box.createHorizontalStrut(10));
        h.add(Box.createHorizontalGlue());
        h.add(text);
        h.setPreferredSize(new Dimension(300,30));
        h.setMaximumSize(new Dimension(300,30));
        return h;
    } // */

    /* Never used!
    private class ForceConstantAction extends AbstractAction {
        public void actionPerformed(ActionEvent arg0) {
            JTextField text = (JTextField)arg0.getSource();
            float val = Float.parseFloat(text.getText());
            Force f = (Force)text.getClientProperty("force");
            Integer param = (Integer)text.getClientProperty("param");
            f.setParameter(param.intValue(), val);
        }
    } // end of inner class ForceAction */

     private class ForceFreezeAction extends AbstractAction {
        public void actionPerformed(ActionEvent arg0) {
            JButton but= (JButton)arg0.getSource();
	    but.setText("Unfreeze");

        }
    }


} // end of class ForcePanel
