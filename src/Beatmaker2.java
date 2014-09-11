import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;

import java.util.HashMap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

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

class BeatMaker extends JFrame implements ActionListener, ChangeListener,
		MouseListener
{
	public JFrame frame;
	private JButton btnKick, btnSnare, btnHiHat, btnClap, btnPlay, btnStop,
			btnSave, btnLoad, btnApply;

	private JCheckBox[] kickBox = new JCheckBox[16];
	private JCheckBox[] snareBox = new JCheckBox[16];
	private JCheckBox[] hatBox = new JCheckBox[16];
	private JCheckBox[] clapBox = new JCheckBox[16];
	private JCheckBox boxSong, boxLoop;

	private JPanel panel;

	String[] ptnList =
	{ "Pattern 0", "Pattern 1", "Pattern 2", "Pattern 3", "Pattern 4",
			"Pattern 5", "Pattern 6", "Pattern 7", "Pattern 8", "Pattern 9" };
	private JComboBox ptnComBox;
	private int currentPtn = 0;

	private JTextField ptnField;

	private JLabel lblBpm;

	private JSpinner spinnerBpm;

	private JSlider playBar;

	private Pattern[] ptn;

	private boolean playing = false, loopFlag = false;

	private JLabel txt, txt2, txt3;

	private int bpm, numOfPtns;
	private int fieldClickCnt = 0;

	private int[] ptnStream = new int[100];

	public BeatMaker()
	{
		Initialize();

	}

	/*
	 * public void playSound(int instNum) { File file = null;
	 * 
	 * switch (instNum) { case 1: file = kickFile; break; case 2: file =
	 * snareFile; break; case 3: file = hatFile; break; case 4: file = clapFile;
	 * break; } try { AudioInputStream AIS =
	 * AudioSystem.getAudioInputStream(file); Clip clip = AudioSystem.getClip();
	 * clip.open(AIS); clip.start(); } catch (Exception ex) {
	 * System.out.println("Error with playing sound."); ex.printStackTrace(); }
	 * }
	 */

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
				}
				while (elapTime <= oneLoopTime && playing == true)
				{
					cntTime = System.currentTimeMillis();
					elapTime = cntTime - startTime;
					if (elapTime == (oneLoopTime / 16) * i)
					{
						playBar.setValue(i - 1);
						txt.setText(i + "");
						if (ptn[currentPtn].kickFlag[i - 1] == true)
						{
							(new BeatPlayer(1)).start();
							/*
							 * Player p = new Player(1); p.start();
							 */
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
		frame.setTitle("BeatMaker_DEMO_v004");
		frame.setBounds(100, 100, 480, 333);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(null);

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

		frame.getContentPane().add(btnClap);
		frame.getContentPane().add(btnHiHat);
		frame.getContentPane().add(btnSnare);
		frame.getContentPane().add(btnKick);

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
		btnPlay.setBounds(245, 70, 50, 21);
		frame.getContentPane().add(btnPlay);
		btnPlay.addActionListener(this);

		btnStop = new JButton("\u25A0");
		btnStop.setBounds(300, 70, 50, 21);
		frame.getContentPane().add(btnStop);
		btnStop.addActionListener(this);

		playBar = new JSlider();
		playBar.setBounds(93, 100, 354, 21);
		playBar.setValue(9);
		playBar.setMaximum(15);
		frame.getContentPane().add(playBar);

		ptnComBox = new JComboBox(ptnList);
		ptnComBox.setToolTipText("Pattern");
		ptnComBox.setBounds(14, 70, 80, 21);
		frame.getContentPane().add(ptnComBox);
		ptnComBox.addActionListener(this);

		txt = new JLabel();
		txt.setForeground(Color.red);
		txt.setBounds(300, 235, 100, 21);
		frame.getContentPane().add(txt);

		ptnField = new JTextField();
		ptnField.setText("패턴을 입력하세요 ex)1123");
		ptnField.setBounds(14, 40, 240, 21);
		frame.getContentPane().add(ptnField);
		ptnField.setColumns(100);
		ptnField.addActionListener(this);
		ptnField.addMouseListener(this);

		btnApply = new JButton("Apply");
		btnApply.setBounds(260, 40, 70, 21);
		frame.getContentPane().add(btnApply);
		btnApply.addActionListener(this);

		boxSong = new JCheckBox("song");
		boxSong.setBounds(14, 10, 60, 21);
		frame.getContentPane().add(boxSong);
		boxSong.addActionListener(this);

		boxLoop = new JCheckBox("loop");
		boxLoop.setBounds(80, 10, 60, 21);
		frame.getContentPane().add(boxLoop);
		boxLoop.addActionListener(this);

		boxLoop.setSelected(true);
		loopFlag = true;

		txt2 = new JLabel("키보드2~=,Q~]까지 피아노 옥타브변경은 Z:저음 X:중음 C:고음");
		txt2.setForeground(Color.red);
		txt2.setBounds(50, 265, 400, 30);
		frame.getContentPane().add(txt2);

		txt3 = new JLabel("피아노 치려면 이쪽(아래쪽) 영역을 클릭");
		txt3.setForeground(Color.red);
		txt3.setBounds(50, 245, 400, 30);
		frame.getContentPane().add(txt3);

		JPanel pianoPanel = new MelodyPanel();
		frame.getContentPane().add(pianoPanel);

	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

		Object ee;
		int i;
		if (e.getSource() == btnPlay)
		{
			(new PlayThread()).start();
			//(new MelodyPlayer(1000)).start();
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
		}

		if (e.getSource() == boxSong)
		{
			boxSong.setSelected(true);
			boxLoop.setSelected(false);
			loopFlag = false;
			getPatternStream();
		}
		if (e.getSource() == boxLoop)
		{
			boxLoop.setSelected(true);
			boxSong.setSelected(false);
			loopFlag = true;
		}

		if (e.getSource() == btnStop)
			playing = false;

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
		if (e.getSource() == ptnComBox) // pattern 선택박스를 수정하면 각각의 CheckBox들을 현재
										// 패턴에 맞게 수정
		{
			currentPtn = ptnComBox.getSelectedIndex();
			txt.setText(currentPtn + "");
			refreshPatternBox(currentPtn);
		}

		if (e.getSource() == ptnField)
		{
			getPatternStream();
		}
		if (e.getSource() == btnApply)
		{
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
		if (e.getSource() == ptnField)
		{

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
	}

	public void refreshPatternBox(int currentPtn)
	{
		for (int i = 0; i < 16; i++)
		{
			kickBox[i].setSelected(ptn[currentPtn].kickFlag[i]);
			snareBox[i].setSelected(ptn[currentPtn].snareFlag[i]);
			hatBox[i].setSelected(ptn[currentPtn].hatFlag[i]);
			clapBox[i].setSelected(ptn[currentPtn].clapFlag[i]);
		}
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
		playing=false;//재생멈춤
		FileDialog saveFile = new FileDialog(this, "Save", FileDialog.SAVE);
		saveFile.setFile("*.bmf");
		saveFile.setDirectory("\\PatternFile");
		saveFile.setVisible(true);

		String fileName = saveFile.getDirectory() + saveFile.getFile();

		int[][][] numPtns = new int[10][4][16];

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
			}
		}

		String fileStream = "";
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					fileStream = fileStream + numPtns[i][j][k];
				}
				fileStream = fileStream + " ";
			}
			fileStream = fileStream + "\n";
		}

		fileStream = ptnField.getText() + "\n" + fileStream;

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
		playing=false;
		FileDialog loadFile = new FileDialog(this, "Load", FileDialog.LOAD);
		loadFile.setFile("*.bmf");
		loadFile.setDirectory("\\PatternFile");
		loadFile.setVisible(true);
		String fileName = loadFile.getDirectory() + loadFile.getFile();
		if (loadFile.getFile() != null)
		{
			fieldClickCnt = 1; //처음부터 패턴을 불러왔을때는 패턴입력필드를 클릭해도 내용이 사라지지 않게함

			RandomAccessFile file = new RandomAccessFile(new File(fileName),
					"rw");
			
			

			String str = null;
			str = file.readLine();
			char temp;
			ptnField.setText(str);
			getPatternStream();
			int[][][] numPtns = new int[10][4][16];

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
					numPtns[i][1][j-17] = (int) temp - 48;
					j++;
				}
				j++;
				while (j < 50)
				{
					temp = str.charAt(j);
					numPtns[i][2][j-34] = (int) temp - 48;
					j++;
				}
				j++;
				while (j < 67)
				{
					temp = str.charAt(j);
					numPtns[i][3][j-51] = (int) temp - 48;
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
				}
			}

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

		if (arg0.getSource() == ptnField && fieldClickCnt == 0)
		{
			ptnField.setText("");
			fieldClickCnt++;
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

}

/*
 * class SaveLoad { BeatMaker b=new BeatMaker(); public void Load() { FileDialog
 * retFile = new FileDialog(b, "파일선택", FileDialog.LOAD); // 파일 Dialog visible
 * retFile.setVisible(true); String fileName = retFile.getDirectory() +
 * retFile.getFile();
 * 
 * try { // file dialog 에서 입력받은 파일명으로 파일 생성 File file = new File(fileName); //
 * output stream 생성 BufferedWriter out = new BufferedWriter(new
 * FileWriter(file)); // 파일쓰기 } catch(Exception ex) { System.err.println(ex); }
 * } }
 */

class BeatPlayer extends Thread
{

	private static final int EXTERNAL_BUFFER_SIZE = 128000;
	private File kickFile = new File("kick.wav").getAbsoluteFile();
	private File snareFile = new File("snare.wav").getAbsoluteFile();
	private File hatFile = new File("hat.wav").getAbsoluteFile();
	private File clapFile = new File("clap.wav").getAbsoluteFile();
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
		}
	}

	public void run()
	{// 메소드

		Clip clip;
		try
		{
			AudioInputStream audioInputStream = AudioSystem // wav파일 재생부-출처를
															// 잊었는데
															// stackoverflow.com에서
															// 참조
					.getAudioInputStream(file);
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

	public MelodyPlayer(int ch)
	{
		fileName = ch + ".wav";
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
			/*
			 * AudioInputStream AIS = AudioSystem.getAudioInputStream(new
			 * File(fileName).getAbsoluteFile()); Clip clip =
			 * AudioSystem.getClip(); clip.open(AIS); clip.start();
			 * 
			 * clip.close();
			 */
		} catch (Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}

	}

}

class MelodyPanel extends JPanel implements KeyListener, MouseListener
{

	private int ocv = 12;// 옥타브설정변수 0일때 저음 12일때 중음 24일때 고음

	public MelodyPanel()
	{
		setBounds(0, 229, 495, 66);
		addKeyListener(this);
		addMouseListener(this);
		Initialize();
	}

	public void Initialize()
	{

	}

	public void keyPressed(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		int keycode = (int) arg0.getKeyCode();

		switch (keycode)
		{
			case KeyEvent.VK_Q:
				MelodyPlayer p1 = new MelodyPlayer(1 + ocv);
				p1.start();
				break;
			case KeyEvent.VK_2:
				(new MelodyPlayer(2 + ocv)).start();
				break;
			case KeyEvent.VK_W:
				(new MelodyPlayer(3 + ocv)).start();
				break;
			case KeyEvent.VK_3:
				(new MelodyPlayer(4 + ocv)).start();
				break;
			case KeyEvent.VK_E:
				(new MelodyPlayer(5 + ocv)).start();
				break;
			case KeyEvent.VK_R:
				(new MelodyPlayer(6 + ocv)).start();
				break;
			case KeyEvent.VK_5:
				(new MelodyPlayer(7 + ocv)).start();
				break;
			case KeyEvent.VK_T:
				(new MelodyPlayer(8 + ocv)).start();
				break;
			case KeyEvent.VK_6:
				(new MelodyPlayer(9 + ocv)).start();
				break;
			case KeyEvent.VK_Y:
				(new MelodyPlayer(10 + ocv)).start();
				break;
			case KeyEvent.VK_7:
				(new MelodyPlayer(11 + ocv)).start();
				break;
			case KeyEvent.VK_U:
				(new MelodyPlayer(12 + ocv)).start();
				break;
			case KeyEvent.VK_I:
				(new MelodyPlayer(13 + ocv)).start();
				break;
			case KeyEvent.VK_9:
				(new MelodyPlayer(14 + ocv)).start();
				break;
			case KeyEvent.VK_O:
				(new MelodyPlayer(15 + ocv)).start();
				break;
			case KeyEvent.VK_0:
				(new MelodyPlayer(16 + ocv)).start();
				break;
			case KeyEvent.VK_P:
				(new MelodyPlayer(17 + ocv)).start();
				break;
			case KeyEvent.VK_OPEN_BRACKET:
				(new MelodyPlayer(18 + ocv)).start();
				break;
			case KeyEvent.VK_EQUALS:
				(new MelodyPlayer(19 + ocv)).start();
				break;
			case KeyEvent.VK_CLOSE_BRACKET:
				(new MelodyPlayer(20 + ocv)).start();
				break;
			case KeyEvent.VK_BACK_SLASH:
				(new MelodyPlayer(21 + ocv)).start();
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
}
