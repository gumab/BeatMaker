import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;

import java.io.*;

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

class BeatMaker extends JFrame implements ActionListener, ChangeListener,
		MouseListener
{
	public JFrame frame,popUp;
	private JButton btnKick, btnSnare, btnHiHat, btnClap, btnInst5, btnInst6,
			btnInst7, btnInst8, btnPlay, btnStop, btnSave, btnLoad, btnApply;

	private JCheckBox[] kickBox = new JCheckBox[16];
	private JCheckBox[] snareBox = new JCheckBox[16];
	private JCheckBox[] hatBox = new JCheckBox[16];
	private JCheckBox[] clapBox = new JCheckBox[16];
	private JCheckBox[] inst5Box = new JCheckBox[16];
	private JCheckBox[] inst6Box = new JCheckBox[16];
	private JCheckBox[] inst7Box = new JCheckBox[16];
	private JCheckBox[] inst8Box = new JCheckBox[16];

	private ImageIcon keyGuide;
	private JLabel imageLabel;

	private JCheckBox boxSong, boxLoop;

	private String[] ptnList =
	{ "Pattern 0", "Pattern 1", "Pattern 2", "Pattern 3", "Pattern 4",
			"Pattern 5", "Pattern 6", "Pattern 7", "Pattern 8", "Pattern 9" };
	private String[] drumList =
	{ "HipHop1", "HipHop2", "HipHop3", "Classic1", "Classic2", "Dance" };
	private String[] melodyList =
	{ "Piano", "Base1", "Base2", "Poing", "Tinkle", "Effects" };

	private JComboBox ptnComBox, drumComBox;
	public static JComboBox melodyComBox;

	private int bpm, numOfPtns;
	private int fieldClickCnt = 0;
	private int currentPtn = 0;
	public static int currentDrum = 0, currentMelody = 0;

	private JProgressBar progressBar;

	private JTextField ptnField;

	private JLabel lblBpm;

	private JSpinner spinnerBpm;

	private JSlider playBar;

	private Pattern[] ptn;

	public static boolean playing;
	private boolean loopFlag = false;

	private JLabel txtCount;

	private int[] ptnStream = new int[500];

	private JPanel melodyPanel;

	public BeatMaker()
	{
		Initialize();
	}

	public void Initialize()
	{
		ptn = new Pattern[10];
		for (int i = 0; i < 10; i++)
		{
			ptn[i] = new Pattern();
		}

		frame = new JFrame();
		frame.setTitle("BeatMaker_DEMO_v006");
		frame.setBounds(100, 100, 480, 570);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addMouseListener(this);

		frame.getContentPane().setLayout(null);
		
		popUp = new JFrame();
		popUp.setTitle("Error");
		popUp.setBounds(640, 360, 200, 100);
		popUp.setResizable(false);

		
		
		
		btnKick = new JButton("Kick");
		btnKick.setToolTipText("hear Kick sound");
		btnKick.setBounds(14, 123, 73, 23);
		btnKick.addActionListener(this);

		btnSnare = new JButton("Snare");
		btnSnare.setToolTipText("hear Snare sound");
		btnSnare.setBounds(14, 153, 73, 23);
		btnSnare.addActionListener(this);

		btnHiHat = new JButton("Hi-Hat");
		btnHiHat.setToolTipText("hear Hi-Hat sound");
		btnHiHat.setBounds(14, 183, 73, 23);
		btnHiHat.addActionListener(this);

		btnClap = new JButton("Clap");
		btnClap.setToolTipText("hear Clap sound");
		btnClap.setBounds(14, 213, 73, 23);
		btnClap.addActionListener(this);

		btnInst5 = new JButton("Inst5");
		btnInst5.setToolTipText("hear Inst5 sound");
		btnInst5.setBounds(14, 243, 73, 23);
		btnInst5.addActionListener(this);

		btnInst6 = new JButton("Inst6");
		btnInst6.setToolTipText("hear Inst6 sound");
		btnInst6.setBounds(14, 273, 73, 23);
		btnInst6.addActionListener(this);

		btnInst7 = new JButton("Inst7");
		btnInst7.setToolTipText("hear Inst7 sound");
		btnInst7.setBounds(14, 303, 73, 23);
		btnInst7.addActionListener(this);

		btnInst8 = new JButton("Inst8");
		btnInst8.setToolTipText("hear Inst8 sound");
		btnInst8.setBounds(14, 333, 73, 23);
		btnInst8.addActionListener(this);

		frame.getContentPane().add(btnClap);
		frame.getContentPane().add(btnHiHat);
		frame.getContentPane().add(btnSnare);
		frame.getContentPane().add(btnKick);
		frame.getContentPane().add(btnInst5);
		frame.getContentPane().add(btnInst6);
		frame.getContentPane().add(btnInst7);
		frame.getContentPane().add(btnInst8);

		int i;
		for (i = 0; i < 16; i++)
		{
			kickBox[i] = new JCheckBox("");
		}
		kickBox[0].setBounds(93, 125, 21, 21);
		kickBox[1].setBounds(114, 125, 21, 21);
		kickBox[2].setBounds(135, 125, 21, 21);
		kickBox[3].setBounds(156, 125, 21, 21);
		kickBox[4].setBounds(183, 125, 21, 21);
		kickBox[5].setBounds(204, 125, 21, 21);
		kickBox[6].setBounds(225, 125, 21, 21);
		kickBox[7].setBounds(246, 125, 21, 21);
		kickBox[8].setBounds(273, 125, 21, 21);
		kickBox[9].setBounds(294, 125, 21, 21);
		kickBox[10].setBounds(315, 125, 21, 21);
		kickBox[11].setBounds(336, 125, 21, 21);
		kickBox[12].setBounds(363, 125, 21, 21);
		kickBox[13].setBounds(384, 125, 21, 21);
		kickBox[14].setBounds(405, 125, 21, 21);
		kickBox[15].setBounds(426, 125, 21, 21);
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
		snareBox[0].setBounds(93, 155, 21, 21);
		snareBox[1].setBounds(114, 155, 21, 21);
		snareBox[2].setBounds(135, 155, 21, 21);
		snareBox[3].setBounds(156, 155, 21, 21);
		snareBox[4].setBounds(183, 155, 21, 21);
		snareBox[5].setBounds(204, 155, 21, 21);
		snareBox[6].setBounds(225, 155, 21, 21);
		snareBox[7].setBounds(246, 155, 21, 21);
		snareBox[8].setBounds(273, 155, 21, 21);
		snareBox[9].setBounds(294, 155, 21, 21);
		snareBox[10].setBounds(315, 155, 21, 21);
		snareBox[11].setBounds(336, 155, 21, 21);
		snareBox[12].setBounds(363, 155, 21, 21);
		snareBox[13].setBounds(384, 155, 21, 21);
		snareBox[14].setBounds(405, 155, 21, 21);
		snareBox[15].setBounds(426, 155, 21, 21);
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
		hatBox[0].setBounds(93, 185, 21, 21);
		hatBox[1].setBounds(114, 185, 21, 21);
		hatBox[2].setBounds(135, 185, 21, 21);
		hatBox[3].setBounds(156, 185, 21, 21);
		hatBox[4].setBounds(183, 185, 21, 21);
		hatBox[5].setBounds(204, 185, 21, 21);
		hatBox[6].setBounds(225, 185, 21, 21);
		hatBox[7].setBounds(246, 185, 21, 21);
		hatBox[8].setBounds(273, 185, 21, 21);
		hatBox[9].setBounds(294, 185, 21, 21);
		hatBox[10].setBounds(315, 185, 21, 21);
		hatBox[11].setBounds(336, 185, 21, 21);
		hatBox[12].setBounds(363, 185, 21, 21);
		hatBox[13].setBounds(384, 185, 21, 21);
		hatBox[14].setBounds(405, 185, 21, 21);
		hatBox[15].setBounds(426, 185, 21, 21);

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
		clapBox[0].setBounds(93, 215, 21, 21);
		clapBox[1].setBounds(114, 215, 21, 21);
		clapBox[2].setBounds(135, 215, 21, 21);
		clapBox[3].setBounds(156, 215, 21, 21);
		clapBox[4].setBounds(183, 215, 21, 21);
		clapBox[5].setBounds(204, 215, 21, 21);
		clapBox[6].setBounds(225, 215, 21, 21);
		clapBox[7].setBounds(246, 215, 21, 21);
		clapBox[8].setBounds(273, 215, 21, 21);
		clapBox[9].setBounds(294, 215, 21, 21);
		clapBox[10].setBounds(315, 215, 21, 21);
		clapBox[11].setBounds(336, 215, 21, 21);
		clapBox[12].setBounds(363, 215, 21, 21);
		clapBox[13].setBounds(384, 215, 21, 21);
		clapBox[14].setBounds(405, 215, 21, 21);
		clapBox[15].setBounds(426, 215, 21, 21);

		for (i = 0; i < 16; i++)
		{
			frame.getContentPane().add(clapBox[i]);
			clapBox[i].addActionListener(this);
		}

		inst5Box = new JCheckBox[16];
		for (i = 0; i < 16; i++)
		{
			inst5Box[i] = new JCheckBox("");
		}
		inst5Box[0].setBounds(93, 245, 21, 21);
		inst5Box[1].setBounds(114, 245, 21, 21);
		inst5Box[2].setBounds(135, 245, 21, 21);
		inst5Box[3].setBounds(156, 245, 21, 21);
		inst5Box[4].setBounds(183, 245, 21, 21);
		inst5Box[5].setBounds(204, 245, 21, 21);
		inst5Box[6].setBounds(225, 245, 21, 21);
		inst5Box[7].setBounds(246, 245, 21, 21);
		inst5Box[8].setBounds(273, 245, 21, 21);
		inst5Box[9].setBounds(294, 245, 21, 21);
		inst5Box[10].setBounds(315, 245, 21, 21);
		inst5Box[11].setBounds(336, 245, 21, 21);
		inst5Box[12].setBounds(363, 245, 21, 21);
		inst5Box[13].setBounds(384, 245, 21, 21);
		inst5Box[14].setBounds(405, 245, 21, 21);
		inst5Box[15].setBounds(426, 245, 21, 21);

		for (i = 0; i < 16; i++)
		{
			frame.getContentPane().add(inst5Box[i]);
			inst5Box[i].addActionListener(this);
		}

		inst6Box = new JCheckBox[16];
		for (i = 0; i < 16; i++)
		{
			inst6Box[i] = new JCheckBox("");
		}
		inst6Box[0].setBounds(93, 275, 21, 21);
		inst6Box[1].setBounds(114, 275, 21, 21);
		inst6Box[2].setBounds(135, 275, 21, 21);
		inst6Box[3].setBounds(156, 275, 21, 21);
		inst6Box[4].setBounds(183, 275, 21, 21);
		inst6Box[5].setBounds(204, 275, 21, 21);
		inst6Box[6].setBounds(225, 275, 21, 21);
		inst6Box[7].setBounds(246, 275, 21, 21);
		inst6Box[8].setBounds(273, 275, 21, 21);
		inst6Box[9].setBounds(294, 275, 21, 21);
		inst6Box[10].setBounds(315, 275, 21, 21);
		inst6Box[11].setBounds(336, 275, 21, 21);
		inst6Box[12].setBounds(363, 275, 21, 21);
		inst6Box[13].setBounds(384, 275, 21, 21);
		inst6Box[14].setBounds(405, 275, 21, 21);
		inst6Box[15].setBounds(426, 275, 21, 21);

		for (i = 0; i < 16; i++)
		{
			frame.getContentPane().add(inst6Box[i]);
			inst6Box[i].addActionListener(this);
		}

		inst7Box = new JCheckBox[16];
		for (i = 0; i < 16; i++)
		{
			inst7Box[i] = new JCheckBox("");
		}
		inst7Box[0].setBounds(93, 305, 21, 21);
		inst7Box[1].setBounds(114, 305, 21, 21);
		inst7Box[2].setBounds(135, 305, 21, 21);
		inst7Box[3].setBounds(156, 305, 21, 21);
		inst7Box[4].setBounds(183, 305, 21, 21);
		inst7Box[5].setBounds(204, 305, 21, 21);
		inst7Box[6].setBounds(225, 305, 21, 21);
		inst7Box[7].setBounds(246, 305, 21, 21);
		inst7Box[8].setBounds(273, 305, 21, 21);
		inst7Box[9].setBounds(294, 305, 21, 21);
		inst7Box[10].setBounds(315, 305, 21, 21);
		inst7Box[11].setBounds(336, 305, 21, 21);
		inst7Box[12].setBounds(363, 305, 21, 21);
		inst7Box[13].setBounds(384, 305, 21, 21);
		inst7Box[14].setBounds(405, 305, 21, 21);
		inst7Box[15].setBounds(426, 305, 21, 21);

		for (i = 0; i < 16; i++)
		{
			frame.getContentPane().add(inst7Box[i]);
			inst7Box[i].addActionListener(this);
		}

		inst8Box = new JCheckBox[16];
		for (i = 0; i < 16; i++)
		{
			inst8Box[i] = new JCheckBox("");
		}
		inst8Box[0].setBounds(93, 335, 21, 21);
		inst8Box[1].setBounds(114, 335, 21, 21);
		inst8Box[2].setBounds(135, 335, 21, 21);
		inst8Box[3].setBounds(156, 335, 21, 21);
		inst8Box[4].setBounds(183, 335, 21, 21);
		inst8Box[5].setBounds(204, 335, 21, 21);
		inst8Box[6].setBounds(225, 335, 21, 21);
		inst8Box[7].setBounds(246, 335, 21, 21);
		inst8Box[8].setBounds(273, 335, 21, 21);
		inst8Box[9].setBounds(294, 335, 21, 21);
		inst8Box[10].setBounds(315, 335, 21, 21);
		inst8Box[11].setBounds(336, 335, 21, 21);
		inst8Box[12].setBounds(363, 335, 21, 21);
		inst8Box[13].setBounds(384, 335, 21, 21);
		inst8Box[14].setBounds(405, 335, 21, 21);
		inst8Box[15].setBounds(426, 335, 21, 21);

		for (i = 0; i < 16; i++)
		{
			frame.getContentPane().add(inst8Box[i]);
			inst8Box[i].addActionListener(this);
		}

		lblBpm = new JLabel("BPM");
		lblBpm.setBounds(370, 70, 27, 21);
		frame.getContentPane().add(lblBpm);

		bpm = 120;
		spinnerBpm = new JSpinner();
		spinnerBpm.setBounds(405, 70, 42, 21);
		spinnerBpm.setModel(new SpinnerNumberModel(120, 60, 240, 10));
		frame.getContentPane().add(spinnerBpm);
		spinnerBpm.addChangeListener(this);

		btnLoad = new JButton("Load");
		btnLoad.setBounds(300, 10, 70, 21);
		frame.getContentPane().add(btnLoad);
		btnLoad.addActionListener(this);

		btnSave = new JButton("Save");
		btnSave.setBounds(380, 10, 70, 21);
		frame.getContentPane().add(btnSave);
		btnSave.addActionListener(this);

		btnPlay = new JButton("\u25B6");
		btnPlay.setToolTipText("Play");
		btnPlay.setBounds(245, 70, 50, 21);
		frame.getContentPane().add(btnPlay);
		btnPlay.addActionListener(this);

		btnStop = new JButton("\u25A0");
		btnStop.setToolTipText("Stop");
		btnStop.setBounds(300, 70, 50, 21);
		frame.getContentPane().add(btnStop);
		btnStop.addActionListener(this);

		playBar = new JSlider();
		playBar.setBounds(93, 100, 354, 21);
		playBar.setValue(0);
		playBar.setMaximum(15);
		frame.getContentPane().add(playBar);

		ptnComBox = new JComboBox(ptnList);
		ptnComBox.setToolTipText("패턴 선택");
		ptnComBox.setBounds(14, 70, 80, 21);
		frame.getContentPane().add(ptnComBox);
		ptnComBox.addActionListener(this);

		drumComBox = new JComboBox(drumList);
		drumComBox.setToolTipText("드럼 선택");
		drumComBox.setBounds(100, 70, 80, 21);
		frame.getContentPane().add(drumComBox);
		drumComBox.addActionListener(this);

		ptnField = new JTextField();
		ptnField.setText("패턴을 입력하세요 ex)1123");
		ptnField.setBounds(14, 40, 240, 21);
		frame.getContentPane().add(ptnField);
		ptnField.setColumns(500);
		ptnField.addActionListener(this);
		ptnField.addMouseListener(this);

		btnApply = new JButton("Apply");
		btnApply.setBounds(260, 40, 70, 21);
		frame.getContentPane().add(btnApply);
		btnApply.addActionListener(this);

		boxSong = new JCheckBox("song");
		boxSong.setToolTipText("패턴순서대로 재생");
		boxSong.setBounds(14, 10, 60, 21);
		frame.getContentPane().add(boxSong);
		boxSong.addActionListener(this);

		boxLoop = new JCheckBox("loop");
		boxLoop.setToolTipText("현재 패턴 반복재생");
		boxLoop.setBounds(80, 10, 60, 21);
		frame.getContentPane().add(boxLoop);
		boxLoop.addActionListener(this);
		boxLoop.setSelected(true);
		loopFlag = true;

		melodyComBox = new JComboBox(melodyList);
		melodyComBox.setToolTipText("멜로디악기 선택");
		melodyComBox.setBounds(14, 363, 73, 21);
		frame.getContentPane().add(melodyComBox);
		melodyComBox.addActionListener(this);

		progressBar = new JProgressBar();
		progressBar.setBounds(93, 366, 355, 15);
		frame.getContentPane().add(progressBar);
		
		txtCount= new JLabel("0/0");
		txtCount.setBounds(93, 380, 50, 15);
		frame.getContentPane().add(txtCount);

		/*keyGuide = new ImageIcon("piano.jpg");
		imageLabel = new JLabel(keyGuide);
		imageLabel.setToolTipText("멜로디 기능을 이용하려면 클릭하세요");
		imageLabel.setBounds(0, 388, 466, 142);
		frame.getContentPane().add(imageLabel);
		imageLabel.addMouseListener(this);
		imageLabel.setVisible(false);*/

		melodyPanel = new MelodyPanel();
		frame.getContentPane().add(melodyPanel);
		

	}

	class PlayThread extends Thread
	{
		private long startTime;
		private long elapTime;
		private long cntTime;
		private long oneLoopTime;
		private int i;
		private int count;

		public void run()
		{

			playing = true;
			count = 0;
			progressBar.setValue(0);
			while (playing == true && (count < numOfPtns || loopFlag == true))
			{
				i = 1;
				startTime = System.currentTimeMillis();
				elapTime = 0;
				cntTime = startTime;
				oneLoopTime = 240000 / bpm;
				if (loopFlag == false)
				{
					
					currentPtn = ptnStream[count];
					refreshPatternBox(currentPtn);
					ptnComBox.setSelectedIndex(currentPtn);
					count++;
					txtCount.setText(count+"/"+numOfPtns);
					

				}
				while (elapTime <= oneLoopTime && playing == true)
				{

					cntTime = System.currentTimeMillis();
					elapTime = cntTime - startTime;
					if (elapTime == (oneLoopTime / 16) * i)
					{
						if (loopFlag == false)
						{
							progressBar.setValue((100 * (count-1) + 100 * i / 16)/ numOfPtns);
							
						}
						playBar.setValue(i - 1);
						if (ptn[currentPtn].kickFlag[i - 1] == true)
						{
							(new BeatPlayer(1)).start();
						}
						if (ptn[currentPtn].snareFlag[i - 1] == true)
						{
							(new BeatPlayer(2)).start();
						}
						if (ptn[currentPtn].hatFlag[i - 1] == true)
						{
							(new BeatPlayer(3)).start();
						}
						if (ptn[currentPtn].clapFlag[i - 1] == true)
						{
							(new BeatPlayer(4)).start();
						}
						if (ptn[currentPtn].inst5Flag[i - 1] == true)
						{
							(new BeatPlayer(5)).start();
						}
						if (ptn[currentPtn].inst6Flag[i - 1] == true)
						{
							(new BeatPlayer(6)).start();
						}
						if (ptn[currentPtn].inst7Flag[i - 1] == true)
						{
							(new BeatPlayer(7)).start();
						}
						if (ptn[currentPtn].inst8Flag[i - 1] == true)
						{
							(new BeatPlayer(8)).start();
						}

						i++;
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		Object ee;
		int i;
		if (e.getSource() == btnPlay)
		{
			playing = false;// 기존에 재생스레드를 멈춤
			getPatternStream();// 패턴순서를 바꾸고 바로 재생할때도 수정한 패턴을 적용시킬수 있게 한다
			(new PlayThread()).start();
			// (new MelodyPlayer(1000)).start();
		}

		for (i = 0; i < 16; i++)
		{
			if (e.getSource() == kickBox[i])
				ptn[currentPtn].kickFlag[i] = kickBox[i].isSelected();
			if (e.getSource() == snareBox[i])
				ptn[currentPtn].snareFlag[i] = snareBox[i].isSelected();
			if (e.getSource() == hatBox[i])
				ptn[currentPtn].hatFlag[i] = hatBox[i].isSelected();
			if (e.getSource() == clapBox[i])
				ptn[currentPtn].clapFlag[i] = clapBox[i].isSelected();
			if (e.getSource() == inst5Box[i])
				ptn[currentPtn].inst5Flag[i] = inst5Box[i].isSelected();
			if (e.getSource() == inst6Box[i])
				ptn[currentPtn].inst6Flag[i] = inst6Box[i].isSelected();
			if (e.getSource() == inst7Box[i])
				ptn[currentPtn].inst7Flag[i] = inst7Box[i].isSelected();
			if (e.getSource() == inst8Box[i])
				ptn[currentPtn].inst8Flag[i] = inst8Box[i].isSelected();
		}

		if (e.getSource() == boxSong)
		{
			boxSong.setSelected(true);
			boxLoop.setSelected(false);
			loopFlag = false;
		}
		if (e.getSource() == boxLoop)
		{
			boxLoop.setSelected(true);
			boxSong.setSelected(false);
			loopFlag = true;
		}

		if (e.getSource() == btnStop)
		{
			playing = false;
			progressBar.setValue(0);
			playBar.setValue(0);
		}

		if (e.getSource() == btnKick)
		{
			(new BeatPlayer(1)).start();
		}
		if (e.getSource() == btnSnare)
		{
			(new BeatPlayer(2)).start();
		}
		if (e.getSource() == btnHiHat)
		{
			(new BeatPlayer(3)).start();
		}
		if (e.getSource() == btnClap)
		{
			(new BeatPlayer(4)).start();
		}
		if (e.getSource() == btnInst5)
		{
			(new BeatPlayer(5)).start();
		}
		if (e.getSource() == btnInst6)
		{
			(new BeatPlayer(6)).start();
		}
		if (e.getSource() == btnInst7)
		{
			(new BeatPlayer(7)).start();
		}
		if (e.getSource() == btnInst8)
		{
			(new BeatPlayer(8)).start();
		}

		if (e.getSource() == ptnComBox) // pattern 선택박스를 수정하면 각각의 CheckBox들을 현재
										// 패턴에 맞게 수정
		{
			currentPtn = ptnComBox.getSelectedIndex();
			refreshPatternBox(currentPtn);
		}

		if (e.getSource() == ptnField)
		{
			getPatternStream();
		}
		if (e.getSource() == btnApply)
		{
			playing = false;// 새로운 패턴순서를 적용하기 전에 재생을 멈춘다.
			
			getPatternStream();
			
		}

		if (e.getSource() == btnLoad)
		{

			try
			{
				Load();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getSource() == btnSave)
		{
			Save();
		}
		if (e.getSource() == drumComBox)
		{
			currentDrum = drumComBox.getSelectedIndex();
		}

		if (e.getSource() == melodyComBox)
		{
			currentMelody = melodyComBox.getSelectedIndex();
			melodyPanel.requestFocus();
		}

	}
	

	public void getPatternStream()
	{
		int i;
		char temp;
		numOfPtns = ptnField.getText().length();
		for (i = 0; i < numOfPtns; i++)// char->int 형변환 출처:
										// http://blog.naver.com/ktwforce?Redirect=Log&logNo=70106537418
		{
			temp = ptnField.getText().charAt(i);
			ptnStream[i] = (int) temp - 48;
		}
		txtCount.setText("0/"+numOfPtns);
	}

	public void refreshPatternBox(int currentPtn)
	{
		for (int i = 0; i < 16; i++)
		{
			kickBox[i].setSelected(ptn[currentPtn].kickFlag[i]);
			snareBox[i].setSelected(ptn[currentPtn].snareFlag[i]);
			hatBox[i].setSelected(ptn[currentPtn].hatFlag[i]);
			clapBox[i].setSelected(ptn[currentPtn].clapFlag[i]);
			inst5Box[i].setSelected(ptn[currentPtn].inst5Flag[i]);
			inst6Box[i].setSelected(ptn[currentPtn].inst6Flag[i]);
			inst7Box[i].setSelected(ptn[currentPtn].inst7Flag[i]);
			inst8Box[i].setSelected(ptn[currentPtn].inst8Flag[i]);
		}
	}

	public int getCurrentDrum()
	{
		return currentDrum;
	}

	public static void setPlayingTrue()
	{
		playing=true;
	}
	public static void setCurrentMelody(int index)
	{
		currentMelody = index;
		melodyComBox.setSelectedIndex(currentMelody);
	}

	@Override
	public void stateChanged(ChangeEvent arg0)
	{
		// TODO Auto-generated method stub
		if (arg0.getSource() == spinnerBpm)
		{
			bpm = Integer.parseInt(spinnerBpm.getValue().toString());
		}

	}

	public void Save()
	{
		playing = false;// 재생멈춤
		FileDialog saveFile = new FileDialog(this, "Save", FileDialog.SAVE);
		saveFile.setFile("*.bmf");
		saveFile.setVisible(true);

		String fileName = saveFile.getDirectory() + saveFile.getFile();

		int[][][] numPtns = new int[10][8][16];

		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 16; j++)
			{

				if (ptn[i].kickFlag[j])
					numPtns[i][0][j] = 1;
				else
					numPtns[i][0][j] = 0;
				if (ptn[i].snareFlag[j])
					numPtns[i][1][j] = 1;
				else
					numPtns[i][1][j] = 0;
				if (ptn[i].hatFlag[j])
					numPtns[i][2][j] = 1;
				else
					numPtns[i][2][j] = 0;
				if (ptn[i].clapFlag[j])
					numPtns[i][3][j] = 1;
				else
					numPtns[i][3][j] = 0;
				if (ptn[i].inst5Flag[j])
					numPtns[i][4][j] = 1;
				else
					numPtns[i][4][j] = 0;
				if (ptn[i].inst6Flag[j])
					numPtns[i][5][j] = 1;
				else
					numPtns[i][5][j] = 0;
				if (ptn[i].inst7Flag[j])
					numPtns[i][6][j] = 1;
				else
					numPtns[i][6][j] = 0;
				if (ptn[i].inst8Flag[j])
					numPtns[i][7][j] = 1;
				else
					numPtns[i][7][j] = 0;
			}
		}

		String fileStream = "";
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					fileStream = fileStream + numPtns[i][j][k];
				}
				fileStream = fileStream + " ";
			}
			fileStream = fileStream + "\n";
		}

		fileStream = currentDrum+"\n"+ bpm+"\n"+ptnField.getText() + "\n" + fileStream;

		BufferedWriter bw = null;
		try
		// 참조 :
		// http://kin.naver.com/qna/detail.nhn?d1id=1&dirId=1040201&docId=76736036&qb=7J6Q67CUIHR4dCDsoIDsnqU=&enc=utf8&section=kin&rank=10&search_sort=0&spq=0
		{
			bw = new BufferedWriter(new FileWriter(fileName));
			bw.write(fileStream);
			bw.flush();
		} catch (IOException ioe)
		{
			System.err.println("파일 작성중 오류발생!");
			ioe.printStackTrace();
		} finally
		{
			if (bw != null)
				try
				{
					bw.close();
				} catch (Exception e)
				{
				}
		}
		

	}

	public void Load() throws IOException
	{
		playing = false;
		FileDialog loadFile = new FileDialog(this, "Load", FileDialog.LOAD);
		loadFile.setFile("*.bmf");
		loadFile.setVisible(true);
		String fileName = loadFile.getDirectory() + loadFile.getFile();
		if (loadFile.getFile() != null)
		{
			fieldClickCnt = 1; // 처음부터 패턴을 불러왔을때는 패턴입력필드를 클릭해도 내용이 사라지지 않게함

			RandomAccessFile file = new RandomAccessFile(new File(fileName),
					"rw");
			char temp;
			String str = null;
			
			str = file.readLine();
			currentDrum=Integer.parseInt(str);
					//(int)str.charAt(0)-48;
			drumComBox.setSelectedIndex(currentDrum);
			
			str=file.readLine();
			
			bpm=Integer.parseInt(str);
			spinnerBpm.setValue(bpm);
			
			str = file.readLine();
			ptnField.setText(str);
			getPatternStream();
			
			
			
			
			int[][][] numPtns = new int[10][8][16];

			int i = 0, j = 0;
			while (i < 10)
			{
				str = file.readLine();
				j = 0;
				while (j < 16)
				{
					temp = str.charAt(j);
					numPtns[i][0][j] = (int) temp - 48;
					j++;
				}
				j++;
				while (j < 33)
				{
					temp = str.charAt(j);
					numPtns[i][1][j - 17] = (int) temp - 48;
					j++;
				}
				j++;
				while (j < 50)
				{
					temp = str.charAt(j);
					numPtns[i][2][j - 34] = (int) temp - 48;
					j++;
				}
				j++;
				while (j < 67)
				{
					temp = str.charAt(j);
					numPtns[i][3][j - 51] = (int) temp - 48;
					j++;
				}
				j++;
				while (j < 84)
				{
					temp = str.charAt(j);
					numPtns[i][4][j - 68] = (int) temp - 48;
					j++;
				}
				j++;
				while (j < 101)
				{
					temp = str.charAt(j);
					numPtns[i][5][j - 85] = (int) temp - 48;
					j++;
				}
				j++;
				while (j < 118)
				{
					temp = str.charAt(j);
					numPtns[i][6][j - 102] = (int) temp - 48;
					j++;
				}
				j++;
				while (j < 135)
				{
					temp = str.charAt(j);
					numPtns[i][7][j - 119] = (int) temp - 48;
					j++;
				}
				i++;
			}

			for (i = 0; i < 10; i++)
			{
				for (j = 0; j < 16; j++)
				{

					if (numPtns[i][0][j] == 1)
						ptn[i].kickFlag[j] = true;
					else
						ptn[i].kickFlag[j] = false;
					if (numPtns[i][1][j] == 1)
						ptn[i].snareFlag[j] = true;
					else
						ptn[i].snareFlag[j] = false;
					if (numPtns[i][2][j] == 1)
						ptn[i].hatFlag[j] = true;
					else
						ptn[i].hatFlag[j] = false;
					if (numPtns[i][3][j] == 1)
						ptn[i].clapFlag[j] = true;
					else
						ptn[i].clapFlag[j] = false;

					if (numPtns[i][4][j] == 1)
						ptn[i].inst5Flag[j] = true;
					else
						ptn[i].inst5Flag[j] = false;
					if (numPtns[i][5][j] == 1)
						ptn[i].inst6Flag[j] = true;
					else
						ptn[i].inst6Flag[j] = false;
					if (numPtns[i][6][j] == 1)
						ptn[i].inst7Flag[j] = true;
					else
						ptn[i].inst7Flag[j] = false;
					if (numPtns[i][7][j] == 1)
						ptn[i].inst8Flag[j] = true;
					else
						ptn[i].inst8Flag[j] = false;
				}
			}

			progressBar.setValue(0);
			playBar.setValue(0);
			currentPtn = ptnStream[0];
			refreshPatternBox(currentPtn);
			ptnComBox.setSelectedIndex(currentPtn);
			file.close(); // 파일을 모두 읽었으면 close(); 한다.
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{

		if (arg0.getSource() == ptnField && fieldClickCnt == 0)// 프로그램 실행하고 처음으로
																// 클릭하면 안내멘트
																// 자동으로 사라지도록함
		{
			ptnField.setText("");
			fieldClickCnt++;
		}
		if (arg0.getSource() == frame)
		{
			melodyPanel.requestFocus();// 아무것도 없는곳을 찍으면 커서가 해제되고 키보드연주가 가능하게 하기
										// 위해 설정함
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

}

class BeatPlayer extends Thread
{
	BeatMaker temp;
	private static final int EXTERNAL_BUFFER_SIZE = 128000;
	private String drumDir = "Wavs/Drum" + temp.currentDrum;
	private File kickFile = new File(drumDir + "/kick.wav").getAbsoluteFile();
	private File snareFile = new File(drumDir + "/snare.wav").getAbsoluteFile();
	private File hatFile = new File(drumDir + "/hat.wav").getAbsoluteFile();
	private File clapFile = new File(drumDir + "/clap.wav").getAbsoluteFile();
	private File inst5File = new File(drumDir + "/inst5.wav").getAbsoluteFile();
	private File inst6File = new File(drumDir + "/inst6.wav").getAbsoluteFile();
	private File inst7File = new File(drumDir + "/inst7.wav").getAbsoluteFile();
	private File inst8File = new File(drumDir + "/inst8.wav").getAbsoluteFile();
	private File file = null;

	public BeatPlayer(int choice)
	{
		switch (choice)
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
			case 5:
				file = inst5File;
				break;
			case 6:
				file = inst6File;
				break;
			case 7:
				file = inst7File;
				break;
			case 8:
				file = inst8File;
				break;
		}
	}

	public void run()
	{// 메소드

		Clip clip;
		try
		{
			// wav파일 재생부-출처를 잊었는데 stackoverflow.com에서 참조
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
			AudioFormat audioFormat = audioInputStream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
					audioFormat);
			SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
			line.start();

			int nBytesRead = 0;
			byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
			while (nBytesRead != -1)
			{
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
				{
					line.write(abData, 0, nBytesRead);
				}
			}
			line.drain();
			line.close();
			audioInputStream.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

class MelodyPlayer extends Thread
{
	private static final int EXTERNAL_BUFFER_SIZE = 128000;
	private String fileName = null;
	BeatMaker temp;

	public MelodyPlayer(int ch,int ocv)
	{
		ch=ch+ocv;
		fileName = "Wavs/Melody" + temp.currentMelody + "/" + ch + ".wav";
	}

	public void run()
	{
		// String fileName = ch + ".wav";
		try
		{
			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(fileName));
			AudioFormat audioFormat = audioInputStream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
					audioFormat);
			SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(audioFormat);
			line.start();

			int nBytesRead = 0;
			byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
			while (nBytesRead != -1&&temp.playing==true)
			{
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
				{
					line.write(abData, 0, nBytesRead);
				}
			}
			line.drain();
			line.close();
			audioInputStream.close();
		} catch (Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}

	}
}

class KeyPointer extends Thread
{
	private MelodyPanel temp;
	public KeyPointer(int i)
	{
		temp.marks[i-1].setVisible(true);
		try
		{
			sleep(300);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp.marks[i-1].setVisible(false);
	}
}

class MelodyPanel extends JPanel implements KeyListener, MouseListener
{
	private BeatMaker temp;
	private JLabel imageLabel;
	private ImageIcon keyGuide;
	public static JLabel[] marks;

	private int ocv = 12;// 옥타브설정변수 0일때 저음 12일때 중음 24일때 고음

	public MelodyPanel()
	{
		setBounds(0, 395, 495, 150);
		addKeyListener(this);
		addMouseListener(this);
		//this.setToolTipText("라벨라벨");
		Initialize();
	}

	public void Initialize()
	{
		this.setLayout(null);
		
		
		marks=new JLabel[20];
		for(int i=0;i<20;i++)
		{
		marks[i]=new JLabel("\u25A0");
		marks[i].setForeground(Color.red);
		this.add(marks[i]);
		marks[i].setVisible(false);
		}
		marks[0].setBounds(20, 105, 20, 20);//Q
		marks[1].setBounds(39, 55, 20, 20);//2
		marks[2].setBounds(58, 105, 20, 20);//W
		marks[3].setBounds(78, 55, 20, 20);//3
		marks[4].setBounds(97, 105, 20, 20);//E
		marks[5].setBounds(135, 105, 20, 20);//R
		marks[6].setBounds(155, 55, 20, 20);//5
		marks[7].setBounds(173, 105, 20, 20);//T
		marks[8].setBounds(193, 55, 20, 20);//6
		marks[9].setBounds(212, 105, 20, 20);//Y
		marks[10].setBounds(232, 55, 20, 20);
		marks[11].setBounds(250, 105, 20, 20);
		marks[12].setBounds(289, 105, 20, 20);
		marks[13].setBounds(309, 55, 20, 20);
		marks[14].setBounds(327, 105, 20, 20);
		marks[15].setBounds(347, 55, 20, 20);
		marks[16].setBounds(365, 105, 20, 20);
		marks[17].setBounds(404, 105, 20, 20);
		marks[18].setBounds(424, 55, 20, 20);
		marks[19].setBounds(442, 105, 20, 20);
		
		keyGuide = new ImageIcon("piano.jpg");
		imageLabel = new JLabel(keyGuide);
		imageLabel.setToolTipText("멜로디 기능을 이용하려면 클릭하세요");
		imageLabel.setBounds(0, 0, 475, 150);
		this.add(imageLabel);
		imageLabel.addMouseListener(this);
		
		
		
		
		
	}

	public void keyPressed(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		int keycode = (int) arg0.getKeyCode();

		temp.setPlayingTrue();
		switch (keycode)
		{
			case KeyEvent.VK_Q:
				(new MelodyPlayer(1,ocv)).start();
				(new KeyPointer(1)).start();
				break;
			case KeyEvent.VK_2:
				(new MelodyPlayer(2,ocv)).start();
				break;
			case KeyEvent.VK_W:
				(new MelodyPlayer(3,ocv)).start();
				break;
			case KeyEvent.VK_3:
				(new MelodyPlayer(4,ocv)).start();
				break;
			case KeyEvent.VK_E:
				(new MelodyPlayer(5,ocv)).start();
				break;
			case KeyEvent.VK_R:
				(new MelodyPlayer(6,ocv)).start();
				break;
			case KeyEvent.VK_5:
				(new MelodyPlayer(7,ocv)).start();
				break;
			case KeyEvent.VK_T:
				(new MelodyPlayer(8,ocv)).start();
				break;
			case KeyEvent.VK_6:
				(new MelodyPlayer(9,ocv)).start();
				break;
			case KeyEvent.VK_Y:
				(new MelodyPlayer(10,ocv)).start();
				break;
			case KeyEvent.VK_7:
				(new MelodyPlayer(11,ocv)).start();
				break;
			case KeyEvent.VK_U:
				(new MelodyPlayer(12,ocv)).start();
				break;
			case KeyEvent.VK_I:
				(new MelodyPlayer(13,ocv)).start();
				break;
			case KeyEvent.VK_9:
				(new MelodyPlayer(14,ocv)).start();
				break;
			case KeyEvent.VK_O:
				(new MelodyPlayer(15,ocv)).start();
				break;
			case KeyEvent.VK_0:
				(new MelodyPlayer(16,ocv)).start();
				break;
			case KeyEvent.VK_P:
				(new MelodyPlayer(17,ocv)).start();
				break;
			case KeyEvent.VK_OPEN_BRACKET:// [key
				(new MelodyPlayer(18,ocv)).start();
				break;
			case KeyEvent.VK_EQUALS:// =key
				(new MelodyPlayer(19,ocv)).start();
				break;
			case KeyEvent.VK_CLOSE_BRACKET:// ]key
				(new MelodyPlayer(20,ocv)).start();
				break;
			case KeyEvent.VK_Z:
				ocv = 0;
				break;
			case KeyEvent.VK_X:
				ocv = 12;
				break;
			case KeyEvent.VK_C:
				ocv = 24;
				break;
			case KeyEvent.VK_A:
				temp.setCurrentMelody(0);
				break;
			case KeyEvent.VK_S:
				temp.setCurrentMelody(1);
				break;
			case KeyEvent.VK_D:
				temp.setCurrentMelody(2);
				break;
			case KeyEvent.VK_F:
				temp.setCurrentMelody(3);
				break;
			case KeyEvent.VK_G:
				temp.setCurrentMelody(4);
				break;
			case KeyEvent.VK_H:
				temp.setCurrentMelody(5);
				break;

		}

	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		// TODO Auto-generated method stub
		this.requestFocus(); // 아래부분을 클릭했을때 키보드로 연주가 가능하도록 Focus를 맞춰준다.

	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

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

}

class Pattern
{
	public boolean[] kickFlag = new boolean[16];
	public boolean[] snareFlag = new boolean[16];
	public boolean[] hatFlag = new boolean[16];
	public boolean[] clapFlag = new boolean[16];
	public boolean[] inst5Flag = new boolean[16];
	public boolean[] inst6Flag = new boolean[16];
	public boolean[] inst7Flag = new boolean[16];
	public boolean[] inst8Flag = new boolean[16];
}
