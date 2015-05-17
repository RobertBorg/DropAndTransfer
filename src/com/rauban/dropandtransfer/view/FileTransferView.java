package com.rauban.dropandtransfer.view;

import com.rauban.dropandtransfer.controller.SessionController;
import com.rauban.dropandtransfer.model.io.FileTransfer;
import com.rauban.dropandtransfer.view.listener.FileTransferListener;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileTransferView extends JPanel {

    private JProgressBar pbTotal;
    private JProgressBar pbCurrent;
    private JLabel lCurrent;
    private JLabel lAvg;
    private JLabel lFileName;
    private SessionController sc;
    private long offerId;
    FileTransferView(FileTransfer ft, SessionController sc) {
        this.sc = sc;
        offerId = ft.getOfferId();
        pbTotal = new JProgressBar(0, 10000);
        pbCurrent = new JProgressBar(0, 10000);
        lCurrent = new JLabel();
        lAvg = new JLabel();
        lFileName = new JLabel();
        ft.addListener(new FileTransferListener() {
            @Override
            public void sessionFileTransferStatusUpdate(long offerId, String currentFile, float currentSpeedInKBytesPerSecond, float currentAvgSpeedInKBytesPerSecond, float currentFilePercent, float currentTransferPercent) {
                lFileName.setText(currentFile);
                lCurrent.setText(String.format("Current speed: %f kBytes/s", currentSpeedInKBytesPerSecond));
                lAvg.setText(String.format("Average speed: %f kBytes/s", currentAvgSpeedInKBytesPerSecond));
                pbCurrent.setValue(Math.round(currentFilePercent * 100));
                pbTotal.setValue(Math.round(currentTransferPercent * 100));
            }
        });
        JButton cancel = new JButton("Cancel");
        final FileTransferView me = this;
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                me.sc.sendCancelRequest(offerId);
            }
        });
        JButton remove = new JButton("Remove");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Container parent = me.getParent();
                        parent.remove(me);
                        parent.repaint();
                    }
                });
            }
        });
        setLayout(new MigLayout());
        add(lFileName, "cell 0 0 4 1");
        add(pbCurrent, "cell 0 1 1 1");
        add(pbTotal,    "cell 1 1 1 1");
        add(cancel,     "cell 2 1 1 1");
        add(remove,     "cell 3 1 1 1");
        add(lCurrent,   "cell 0 2 2 1");
        add(lAvg,       "cell 2 2 2 1");
    }
}
