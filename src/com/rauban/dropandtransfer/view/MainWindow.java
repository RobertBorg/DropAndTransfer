package com.rauban.dropandtransfer.view.newui;

import com.rauban.dropandtransfer.controller.NetworkController;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame
{
    private NetworkController controller;

    public MainWindow(NetworkController controller)
    {
        this.controller = controller;
        this.initializeUI();

    }

    private void initializeUI()
    {
        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setLayout(new GridLayout(5, 5, 5, 5));

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.search();
            }
        });
        panel.add(searchButton);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.start();
            }
        });
        panel.add(startButton);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.stop();
            }
        });
        panel.add(stopButton);


        this.add(panel);
    }


}
