import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

import java.util.HashMap;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.*;

public class Beatmaker2
{
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					BeatMaker window = new BeatMaker();
					window.frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		});
	}
}

class KeyInput extends JPanel implements KeyListener
{

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
}

class BeatMaker extends JFrame implements ActionListener,KeyListener,MouseListener
{
	public JFrame frame;
	private JButton btnKick, btnSnare, btnHiHat, btnClap, btnAdd, btnDel,
			btnPlay, btnStop, btnBpmUp, btnBpmDn;

	private JCheckBox[] kickBox = new JCheckBox[16];
	private JCheckBox[] snareBox = new JCheckBox[16];
	private JCheckBox[] hatBox = new JCheckBox[16];
	private JCheckBox[] clapBox = new JCheckBox[16];
	
	private JPanel panel;

	String[] ptnList =
	{ "Pattern 1", "Pattern 2", "Pattern 3", "Pattern 4", "Pattern 5",
			"Pattern 6", "Pattern 7", "Pattern 8", "Pattern 9", "Pattern 10" };
	private JComboBox comboBox;
	private int cntPtn = 0;
	private int ocv=12;//옥타브바꿔주는거

	private JLabel lblBpm, lblBpmMeter;

	private JSpinner spinnerBpm;

	private JSlider playBar;

	private Pattern[] ptn;

	private boolean playing = false;

	private JLabel txt,txt2,txt3;

	private int bpm;
	private static int EXTERNAL_BUFFER_SIZE = 128000;

	public BeatMaker()
	{
		
	
		Initialize();

	}

	private File kickFile = new File("kick.wav").getAbsoluteFile();
	private File snareFile = new File("snare.wav").getAbsoluteFile();
	private File hatFile = new File("hat.wav").getAbsoluteFile();
	private File clapFile = new File("clap.wav").getAbsoluteFile();

	public void playSound(int instNum)
	{
		File file = null;

		switch (instNum)
		{
			case 1:
				file = kickFile;
				break;
			case 2:
				file = snareFile;
				break;
			case 3:
				file = hatFile;
				break;
			case 4:
				file = clapFile;
				break;
		}
		try
		{
			AudioInputStream AIS = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(AIS);
			clip.start();
		} catch (Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	class PlayThread extends Thread
	{
		private long startTime;
		private long elapTime;
		private long cntTime;
		private long oneLoopTime;
		private int i;
		public void run()
		{
			playing = true;
			while (playing == true)
			{
				i = 1;
				startTime = System.currentTimeMillis();
				elapTime = 0;
				cntTime = startTime;
				oneLoopTime = 240000 / bpm;
				while (elapTime <= oneLoopTime && playing == true)
				{
					cntTime = System.currentTimeMillis();
					elapTime = cntTime - startTime;
					if (elapTime == (oneLoopTime / 16) * i)
					{
						playBar.setValue(i - 1);
						txt.setText(i + "");
						if (ptn[cntPtn].kickFlag[i - 1] == true)
						{
							playSound(1);
							/*
							 * Player p = new Player(1); p.start();
							 */
						}
						if (ptn[cntPtn].snareFlag[i - 1] == true)
						{
							playSound(2);
						}
						if (ptn[cntPtn].hatFlag[i - 1] == true)
						{
							playSound(3);
						}
						if (ptn[cntPtn].clapFlag[i - 1] == true)
						{
							playSound(4);
						}

						i++;
					}
				}
			}
		}
	}

	public void Initialize()
	{
		
		
		ptn = new Pattern[10];
		for (int i = 0; i < 10; i++)
		{
			ptn[i] = new Pattern();
		}

		frame = new JFrame();
		frame.setTitle("BeatMaker_DEMO_v003");
		frame.setBounds(100, 100, 480, 323);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(null);

		btnKick = new JButton("Kick");
		btnKick.setToolTipText("hear Kick sound");
		btnKick.setBounds(14, 113, 73, 23);
		btnKick.addActionListener(this);

		btnSnare = new JButton("Snare");
		btnSnare.setToolTipText("hear Snare sound");
		btnSnare.setBounds(14, 143, 73, 23);
		btnSnare.addActionListener(this);

		btnHiHat = new JButton("Hi-Hat");
		btnHiHat.setToolTipText("hear Hi-Hat sound");
		btnHiHat.setBounds(14, 173, 73, 23);
		btnHiHat.addActionListener(this);

		btnClap = new JButton("Clap");
		btnClap.setToolTipText("hear Clap sound");
		btnClap.setBounds(14, 203, 73, 23);
		btnClap.addActionListener(this);

		frame.getContentPane().add(btnClap);
		frame.getContentPane().add(btnHiHat);
		frame.getContentPane().add(btnSnare);
		frame.getContentPane().add(btnKick);

		int i;
		for (i = 0; i < 16; i++)
		{
			kickBox[i] = new JCheckBox("");
		}
		kickBox[0].setBounds(93, 115, 21, 21);
		kickBox[1].setBounds(114, 115, 21, 21);
		kickBox[2].setBounds(135, 115, 21, 21);
		kickBox[3].setBounds(156, 115, 21, 21);
		kickBox[4].setBounds(183, 115, 21, 21);
		kickBox[5].setBounds(204, 115, 21, 21);
		kickBox[6].setBounds(225, 115, 21, 21);
		kickBox[7].setBounds(246, 115, 21, 21);
		kickBox[8].setBounds(273, 115, 21, 21);
		kickBox[9].setBounds(294, 115, 21, 21);
		kickBox[10].setBounds(315, 115, 21, 21);
		kickBox[11].setBounds(336, 115, 21, 21);
		kickBox[12].setBounds(363, 115, 21, 21);
		kickBox[13].setBounds(384, 115, 21, 21);
		kickBox[14].setBounds(405, 115, 21, 21);
		kickBox[15].setBounds(426, 115, 21, 21);
		for (i = 0; i < 16; i++)
		{
			frame.getContentPane().add(kickBox[i]);
			kickBox[i].addActionListener(this);
		}

		snareBox = new JCheckBox[16];
		for (i = 0; i < 16; i++)
		{
			snareBox[i] = new JCheckBox("");
		}
		snareBox[0].setBounds(93, 145, 21, 21);
		snareBox[1].setBounds(114, 145, 21, 21);
		snareBox[2].setBounds(135, 145, 21, 21);
		snareBox[3].setBounds(156, 145, 21, 21);
		snareBox[4].setBounds(183, 145, 21, 21);
		snareBox[5].setBounds(204, 145, 21, 21);
		snareBox[6].setBounds(225, 145, 21, 21);
		snareBox[7].setBounds(246, 145, 21, 21);
		snareBox[8].setBounds(273, 145, 21, 21);
		snareBox[9].setBounds(294, 145, 21, 21);
		snareBox[10].setBounds(315, 145, 21, 21);
		snareBox[11].setBounds(336, 145, 21, 21);
		snareBox[12].setBounds(363, 145, 21, 21);
		snareBox[13].setBounds(384, 145, 21, 21);
		snareBox[14].setBounds(405, 145, 21, 21);
		snareBox[15].setBounds(426, 145, 21, 21);
		for (i = 0; i < 16; i++)
		{
			frame.getContentPane().add(snareBox[i]);
			snareBox[i].addActionListener(this);
		}

		hatBox = new JCheckBox[16];
		for (i = 0; i < 16; i++)
		{
			hatBox[i] = new JCheckBox("");
		}
		hatBox[0].setBounds(93, 175, 21, 21);
		hatBox[1].setBounds(114, 175, 21, 21);
		hatBox[2].setBounds(135, 175, 21, 21);
		hatBox[3].setBounds(156, 175, 21, 21);
		hatBox[4].setBounds(183, 175, 21, 21);
		hatBox[5].setBounds(204, 175, 21, 21);
		hatBox[6].setBounds(225, 175, 21, 21);
		hatBox[7].setBounds(246, 175, 21, 21);
		hatBox[8].setBounds(273, 175, 21, 21);
		hatBox[9].setBounds(294, 175, 21, 21);
		hatBox[10].setBounds(315, 175, 21, 21);
		hatBox[11].setBounds(336, 175, 21, 21);
		hatBox[12].setBounds(363, 175, 21, 21);
		hatBox[13].setBounds(384, 175, 21, 21);
		hatBox[14].setBounds(405, 175, 21, 21);
		hatBox[15].setBounds(426, 175, 21, 21);

		for (i = 0; i < 16; i++)
		{
			frame.getContentPane().add(hatBox[i]);
			hatBox[i].addActionListener(this);
		}

		clapBox = new JCheckBox[16];
		for (i = 0; i < 16; i++)
		{
			clapBox[i] = new JCheckBox("");
		}
		clapBox[0].setBounds(93, 205, 21, 21);
		clapBox[1].setBounds(114, 205, 21, 21);
		clapBox[2].setBounds(135, 205, 21, 21);
		clapBox[3].setBounds(156, 205, 21, 21);
		clapBox[4].setBounds(183, 205, 21, 21);
		clapBox[5].setBounds(204, 205, 21, 21);
		clapBox[6].setBounds(225, 205, 21, 21);
		clapBox[7].setBounds(246, 205, 21, 21);
		clapBox[8].setBounds(273, 205, 21, 21);
		clapBox[9].setBounds(294, 205, 21, 21);
		clapBox[10].setBounds(315, 205, 21, 21);
		clapBox[11].setBounds(336, 205, 21, 21);
		clapBox[12].setBounds(363, 205, 21, 21);
		clapBox[13].setBounds(384, 205, 21, 21);
		clapBox[14].setBounds(405, 205, 21, 21);
		clapBox[15].setBounds(426, 205, 21, 21);

		for (i = 0; i < 16; i++)
		{
			frame.getContentPane().add(clapBox[i]);
			clapBox[i].addActionListener(this);
		}

		lblBpm = new JLabel("BPM");
		lblBpm.setBounds(363, 55, 27, 22);
		frame.getContentPane().add(lblBpm);

		bpm = 120;
		lblBpmMeter = new JLabel(bpm + "");
		lblBpmMeter.setBounds(395, 55, 40, 22);
		frame.getContentPane().add(lblBpmMeter);

		btnBpmUp = new JButton("▲");
		btnBpmUp.setBounds(425, 54, 20, 10);
		frame.getContentPane().add(btnBpmUp);
		btnBpmUp.addActionListener(this);

		btnBpmDn = new JButton("▼");
		btnBpmDn.setBounds(425, 67, 20, 10);
		frame.getContentPane().add(btnBpmDn);
		btnBpmDn.addActionListener(this);
		/*
		 * spinnerBpm = new JSpinner(); spinnerBpm.setBounds(405, 20, 42, 22);
		 * spinnerBpm.setModel(new SpinnerNumberModel(120, 60, 180, 10));
		 * frame.getContentPane().add(spinnerBpm);
		 * spinnerBpm.addChangeListener(listener)
		 */

		btnAdd = new JButton("Add");
		btnAdd.setBounds(100, 54, 70, 23);
		frame.getContentPane().add(btnAdd);
		btnAdd.addActionListener(this);

		btnPlay = new JButton("\u25B6");
		btnPlay.setBounds(245, 54, 50, 23);
		frame.getContentPane().add(btnPlay);
		btnPlay.addActionListener(this);

		btnStop = new JButton("\u25A0");
		btnStop.setBounds(300, 54, 50, 23);
		frame.getContentPane().add(btnStop);
		btnStop.addActionListener(this);

		playBar = new JSlider();
		playBar.setBounds(93, 86, 354, 23);
		playBar.setValue(9);
		playBar.setMaximum(15);
		frame.getContentPane().add(playBar);

		comboBox = new JComboBox(ptnList);
		comboBox.setToolTipText("Pattern");
		comboBox.setBounds(14, 54, 80, 21);
		frame.getContentPane().add(comboBox);
		comboBox.addActionListener(this);

		txt = new JLabel();
		txt.setForeground(Color.red);
		txt.setBounds(300, 235, 100, 21);
		frame.getContentPane().add(txt);
		
		
		
		txt2 = new JLabel("키보드2~=,Q~]까지 피아노 옥타브변경은 Z:저음 X:중음 C:고음");
		txt2.setForeground(Color.red);
		txt2.setBounds(50, 255, 400, 30);
		frame.getContentPane().add(txt2);
		
		txt3 = new JLabel("피아노 치려면 이쪽(아래쪽) 영역을 클릭");
		txt3.setForeground(Color.red);
		txt3.setBounds(50, 235, 400, 30);
		frame.getContentPane().add(txt3);
		
		panel = new JPanel();
		panel.setBounds(0, 229, 495, 56);
		frame.getContentPane().add(panel);
		panel.addKeyListener(this);
		panel.addMouseListener(this);

	}

	public void changeevent(ChangeEvent cev)
	{
		if (cev.getSource() == spinnerBpm)
		{
			bpm = Integer.parseInt(spinnerBpm.getValue().toString());
			lblBpmMeter.setText(bpm + "");

		}

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		Object ee;
		int i;
		/*
		 * for (i = 0; i < 16; i++) { if (e.getSource() == kickBox[i]) {
		 * kickFlag[i] = !kickFlag[i]; } } for (i = 0; i < 16; i++) { if
		 * (e.getSource() == snareBox[i]) { snareFlag[i] = !snareFlag[i]; } }
		 * for (i = 0; i < 16; i++) { if (e.getSource() == hatBox[i]) {
		 * hatFlag[i] = !hatFlag[i]; } } for (i = 0; i < 16; i++) { if
		 * (e.getSource() == clapBox[i]) { clapFlag[i] = !clapFlag[i]; } }
		 */

		if (e.getSource() == btnBpmUp && bpm >= 60 && bpm < 180)
		{
			bpm = bpm + 1;
			lblBpmMeter.setText(bpm + "");
		}
		if (e.getSource() == btnBpmDn && bpm > 60 && bpm <= 180)
		{
			bpm = bpm - 1;
			lblBpmMeter.setText(bpm + "");
		}

		if (e.getSource() == btnPlay)
		{
			(new PlayThread()).start();
		}

		for (i = 0; i < 16; i++)
		{
			if (e.getSource() == kickBox[i])
				ptn[cntPtn].kickFlag[i] = kickBox[i].isSelected();
			if (e.getSource() == snareBox[i])
				ptn[cntPtn].snareFlag[i] = snareBox[i].isSelected();
			if (e.getSource() == hatBox[i])
				ptn[cntPtn].hatFlag[i] = hatBox[i].isSelected();
			if (e.getSource() == clapBox[i])
				ptn[cntPtn].clapFlag[i] = clapBox[i].isSelected();
		}

		if (e.getSource() == btnStop)
			playing = false;

		if (e.getSource() == btnKick)
		{
			playSound(1);
		}
		if (e.getSource() == btnSnare)
		{
			playSound(2);
		}
		if (e.getSource() == btnHiHat)
		{
			playSound(3);
		}
		if (e.getSource() == btnClap)
		{
			playSound(4);
		}
		if (e.getSource() == comboBox)
		{
			cntPtn = comboBox.getSelectedIndex();
			txt.setText(cntPtn + "");
			for (i = 0; i < 16; i++)
			{
				kickBox[i].setSelected(ptn[cntPtn].kickFlag[i]);
				snareBox[i].setSelected(ptn[cntPtn].snareFlag[i]);
				hatBox[i].setSelected(ptn[cntPtn].hatFlag[i]);
				clapBox[i].setSelected(ptn[cntPtn].clapFlag[i]);
			}
		}

	}


	/*class Piano extends Thread
	{
		String fileName = null;

		public Piano(int ch)
		{
			fileName = ch + ".wav";
		}
*/
		public void Piano(int ch)
		{
			String fileName = ch + ".wav";
			try
			{
				txt.setText("눌림");
				AudioInputStream AIS = AudioSystem.getAudioInputStream(new File(fileName).getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(AIS);
				clip.start();

				
			} catch (Exception ex)
			{
				System.out.println("Error with playing sound.");
				ex.printStackTrace();
			}

		}

	//}

	@Override
	public void keyPressed(KeyEvent e2)
	{
		int keycode = (int) e2.getKeyCode();

		switch (keycode)
		{
			case KeyEvent.VK_Q:
				Piano(1+ocv);
				break;
			case KeyEvent.VK_2:
				Piano(2+ocv);
				break;
			case KeyEvent.VK_W:
				Piano(3+ocv);
				break;
			case KeyEvent.VK_3:
				Piano(4+ocv);
				break;
			case KeyEvent.VK_E:
				Piano(5+ocv);
				break;
			case KeyEvent.VK_R:
				Piano(6+ocv);
				break;
			case KeyEvent.VK_5:
				Piano(7+ocv);
				break;
			case KeyEvent.VK_T:
				Piano(8+ocv);
				break;
			case KeyEvent.VK_6:
				Piano(9+ocv);
				break;
			case KeyEvent.VK_Y:
				Piano(10+ocv);
				break;
			case KeyEvent.VK_7:
				Piano(11+ocv);
				break;
			case KeyEvent.VK_U:
				Piano(12+ocv);
				break;
			case KeyEvent.VK_I:
				Piano(13+ocv);
				break;
			case KeyEvent.VK_9:
				Piano(14+ocv);
				break;
			case KeyEvent.VK_O:
				Piano(15+ocv);
				break;
			case KeyEvent.VK_0:
				Piano(16+ocv);
				break;
			case KeyEvent.VK_P:
				Piano(17+ocv);
				break;
			case KeyEvent.VK_OPEN_BRACKET:
				Piano(18+ocv);
				break;
			case KeyEvent.VK_EQUALS:
				Piano(19+ocv);
				break;
			case KeyEvent.VK_CLOSE_BRACKET:
				Piano(20+ocv);
				break;
			case KeyEvent.VK_BACK_SLASH:
				Piano(21+ocv);
				break;
			/*case KeyEvent.VK_Q:
				(new Piano(1+ocv)).start();
				break;
			case KeyEvent.VK_2:
				(new Piano(2+ocv)).start();
				break;
			case KeyEvent.VK_W:
				(new Piano(3+ocv)).start();
				break;
			case KeyEvent.VK_3:
				(new Piano(4+ocv)).start();
				break;
			case KeyEvent.VK_E:
				(new Piano(5+ocv)).start();
				break;
			case KeyEvent.VK_R:
				(new Piano(6+ocv)).start();
				break;
			case KeyEvent.VK_5:
				(new Piano(7+ocv)).start();
				break;
			case KeyEvent.VK_T:
				(new Piano(8+ocv)).start();
				break;
			case KeyEvent.VK_6:
				(new Piano(9+ocv)).start();
				break;
			case KeyEvent.VK_Y:
				(new Piano(10+ocv)).start();
				break;
			case KeyEvent.VK_7:
				(new Piano(11+ocv)).start();
				break;
			case KeyEvent.VK_U:
				(new Piano(12+ocv)).start();
				break;
			case KeyEvent.VK_I:
				(new Piano(13+ocv)).start();
				break;
			case KeyEvent.VK_9:
				(new Piano(14+ocv)).start();
				break;
			case KeyEvent.VK_O:
				(new Piano(15+ocv)).start();
				break;
			case KeyEvent.VK_0:
				(new Piano(16+ocv)).start();
				break;
			case KeyEvent.VK_P:
				(new Piano(17+ocv)).start();
				break;
			case KeyEvent.VK_OPEN_BRACKET:
				(new Piano(18+ocv)).start();
				break;
			case KeyEvent.VK_EQUALS:
				(new Piano(19+ocv)).start();
				break;
			case KeyEvent.VK_CLOSE_BRACKET:
				(new Piano(20+ocv)).start();
				break;
			case KeyEvent.VK_BACK_SLASH:
				(new Piano(21+ocv)).start();
				break;*/
			case KeyEvent.VK_Z:
				ocv=0;
				break;
			case KeyEvent.VK_X:
				ocv=12;
				break;
			case KeyEvent.VK_C:
				ocv=24;
				break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		panel.requestFocus();		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}

class Pattern
{
	public boolean[] kickFlag = new boolean[16];
	public boolean[] snareFlag = new boolean[16];
	public boolean[] hatFlag = new boolean[16];
	public boolean[] clapFlag = new boolean[16];
}
