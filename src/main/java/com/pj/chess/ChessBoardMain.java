package com.pj.chess;
import static com.pj.chess.ChessConstant.*;
import static com.pj.chess.LogWindow.*;
import static com.pj.chess.Manual.*;
import static com.pj.chess.RecordWindow.addrlog;
import static com.pj.chess.RecordWindow.jtextArea2;
import static com.pj.chess.Tools.seedexp;
import static com.pj.chess.VersionFile.*;
import static com.pj.chess.ccproperties.readProperties;
import static com.pj.chess.ccproperties.writeProperties;
import static com.pj.chess.multiplayer.clienttoclient.*;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.pj.chess.chessmove.ChessMovePlay;
import com.pj.chess.chessmove.MoveNode;
import com.pj.chess.chessparam.ChessParam;
import com.pj.chess.evaluate.EvaluateComputeMiddleGame;
import com.pj.chess.zobrist.TranspositionTable;

public class ChessBoardMain extends JFrame {
    private static final long serialVersionUID = 1L;
    public static  final String[] chessName=new String[]{
            "   ",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
            "�ڽ�","�ڳ�","�ڳ�","����","����","����","����","����","����","��ʿ","��ʿ","����","����","����","����","����",
            "�콫","�쳵","�쳵","����","����","����","����","����","����","��ʿ","��ʿ","����","����","����","����","����",
    };
    public static  final String[] chessIcon=new String[]{
            null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
            "BK","BR","BR","BN","BN","BC","BC","BB","BB","BA","BA","BP","BP","BP","BP","BP",
            "RK","RR","RR","RN","RN","RC","RC","RB","RB","RA","RA","RP","RP","RP","RP","RP",
    };
    int lastTimeCheckedSite=-1;
    private ButtonActionListener my = new ButtonActionListener();
    static JLabel[] buttons=new JLabel[BOARDSIZE90];
    public static Integer play=1;
    static volatile boolean[] android=new boolean[]{false,false};
    int begin=-1;
    int end=0;
    private static ComputerLevel computerLevel=ComputerLevel.greenHand;
    boolean isBackstageThink=false;
    boolean computeFig=false;
    static TranspositionTable  transTable;
    ChessMovePlay cmp=null;
    AICoreHandler _AIThink=new AICoreHandler();
    AICoreHandler backstageAIThink=new AICoreHandler();
    //	public static List<MoveNode> backMove=new ArrayList<MoveNode>();
    static NodeLink moveHistory;
    static int turn_num=0;
    static ChessParam chessParamCont;
    private static boolean isSound=false;
    public static String startFen="c6c5  rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR b - - 0 1";
    public void initHandler(){


//		String startFen="c6c5  9/CP2k4/9/9/9/9/9/9/9/4K4 b - - 0 1";
//		Tools.parseFENtoBoardZobrist(fenStr);


        String[] fenArray = Tools.fenToFENArray(startFen);
        int[] boardTemp = Tools.parseFEN(fenArray[1]);
        //??????????????
        chessParamCont=ChessInitialize.getGlobalChessParam(boardTemp);
        //??????��?????
        clearBoardIcon();
        //???????????
        for(int i=0;i<boardTemp.length;i++){
            if(boardTemp[i]>0){
                this.setBoardIconUnchecked(i,boardTemp[i]);
            }
        }

        transTable=new TranspositionTable() ;
        if(moveHistory==null){
            moveHistory=new NodeLink(1-play,transTable.boardZobrist32,transTable.boardZobrist64);
        }
        play=1-moveHistory.play;
        android[1-play]=true;
        cmp=new ChessMovePlay(chessParamCont,transTable,new EvaluateComputeMiddleGame(chessParamCont));
        addlog("��������: "+startFen);
        Tools.getseed(chessParamCont.board,moveHistory);

    }
    JPanel jpanelContent;
    private void setCenter(){
        if(jpanelContent!=null){
            this.remove(jpanelContent);
        }
        jpanelContent= new javax.swing.JPanel() {
            protected void paintComponent(Graphics g) {
                try {
                    BufferedImage img=ImageIO.read(getClass().getResource("/images/MAIN.GIF"));
                    g.drawImage(img, 0, 0,  null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        this.setLayout(new BorderLayout());

        JPanel panel  = new javax.swing.JPanel();

        jpanelContent.setLayout(new BorderLayout());
        JPanel jpNorth=new JPanel();
        jpNorth.setPreferredSize(new Dimension(25,25));
//		jpNorth.setBackground(Color.white);
        jpNorth.setOpaque(false);
        jpanelContent.add(jpNorth,BorderLayout.NORTH);
        JPanel jpSouth=new JPanel();
        jpSouth.setPreferredSize(new Dimension(5,5));
        jpSouth.setBackground(Color.black);
        jpSouth.setOpaque(false);
        jpanelContent.add(jpSouth,BorderLayout.SOUTH);
        JPanel jpWest=new JPanel();
        jpWest.setPreferredSize(new Dimension(20,20));
        jpWest.setBackground(Color.blue);
        jpWest.setOpaque(false);
        jpanelContent.add(jpWest,BorderLayout.WEST);
        JPanel jpEast=new JPanel();
        jpEast.setPreferredSize(new Dimension(20,20));
        jpEast.setBackground(Color.CYAN);
        jpEast.setOpaque(false);
        jpanelContent.add(jpEast,BorderLayout.EAST);
        panel.setLayout(new GridLayout(10, 9));
        panel.setPreferredSize(new Dimension(100,100));
        panel.setOpaque(false);
        jpanelContent.add(panel,BorderLayout.CENTER);

        for (int i = 0; i < BOARDSIZE90; i++) {
            JLabel  p = new JLabel();
            p.addMouseListener(my);
            p.setBackground(Color.red);
            p.setSize(55, 55);
            buttons[i]=p;
            panel.add(p);
        }
        this.add(jpanelContent,BorderLayout.CENTER);
    }
    public static Button nextstep = new Button("��һ��");
    public static Button laststep = new Button("��һ��");

    public static Button newgame = new Button("����Ϸ");
    public static Button button = new Button("����");
    public static Button computerMove = new Button("AI��������");
    public ChessBoardMain() {

        super("�й�����");
        setCenter();

        //infotolist();




        JPanel constrol=new JPanel();
        constrol.setLayout(new GridLayout(1, 1));
        newgame.addActionListener(my);
        button.addActionListener(my);
        computerMove.addActionListener(my);

        nextstep.addActionListener(my);

        laststep.addActionListener(my);
        constrol.add(newgame);
        constrol.add(button);
        constrol.add(computerMove);
        constrol.add(laststep);
        constrol.add(nextstep);
        manualend();
        this.add(constrol,BorderLayout.SOUTH);

        this.addWindowListener(my);
        //��ʼ��
        initHandler();
        this.setJMenuBar(setJMenuBar());

        this.setSize(568, 680);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }
    public static Integer multiplayer = 0;
    private MenuItemActionListener menuItemAction=new MenuItemActionListener();
    //JRadioButtonMenuItem hashSize2M = new JRadioButtonMenuItem("HASH??��",true);
    //JRadioButtonMenuItem hashSize32M = new JRadioButtonMenuItem("HASH????",false);
    //JRadioButtonMenuItem hashSize64M = new JRadioButtonMenuItem("HASH???",false);
    public static void manualstart(){
        nextstep.setEnabled(true);
        computerMove.setEnabled(false);
        button.setEnabled(false);
        endmanual.setEnabled(true);
    }
    public static void manualend(){
        nextstep.setEnabled(false);
        laststep.setEnabled(false);
        computerMove.setEnabled(true);
        button.setEnabled(true);
        endmanual.setEnabled(false);
    }
    public static JMenuItem endmanual = new JMenuItem("�˳�����");
    public static JMenuItem matchplayer= new JMenuItem("ƥ�����");
    public static JMenuItem personalinfomation= new JMenuItem("������Ϣ");
    public static JMenuItem exitserver= new JMenuItem("ȡ������");
    private JMenuBar setJMenuBar(){
        JMenuBar jmb = new JMenuBar();
        JMenu menu_file = new JMenu("��Ϸ");
        JMenu menu_ai = new JMenu("AI�ȼ�");
        JMenu menu_net = new JMenu("����");
        JMenu menu_manual = new JMenu("����");
        JMenu menu_else = new JMenu("����");
        JMenuItem create = new JMenuItem("�½�");
        JMenuItem save= new JMenuItem("����");
        JMenuItem imports= new JMenuItem("����");
        JMenuItem connectclient= new JMenuItem("�˶Զ�ֱ��");
        JMenuItem connect= new JMenuItem("����������");
        JMenuItem seedimport= new JMenuItem("���ӵ���");
        JMenuItem seedexport= new JMenuItem("���ӵ���");
        JMenuItem importmanual = new JMenuItem("��������");
        JMenuItem exportmanual = new JMenuItem("��������");
        JMenuItem logwindow = new JMenuItem("����־��¼����");
        JMenuItem recordwindow = new JMenuItem("��AI�����������");
        JMenuItem aboutme = new JMenuItem("����");
        JMenuItem exitthis = new JMenuItem("�˳�");
        JMenuItem aitoai = new JMenuItem("�ۿ�AI����");
        endmanual.setEnabled(false);
        JRadioButtonMenuItem mi_6 = new JRadioButtonMenuItem("����",true);
        JRadioButtonMenuItem mi_7 = new JRadioButtonMenuItem("����",false);
        JRadioButtonMenuItem mi_8 = new JRadioButtonMenuItem("ҵ��",false);
        JRadioButtonMenuItem mi_9 = new JRadioButtonMenuItem("ר��",false);
        JRadioButtonMenuItem mi_10 = new JRadioButtonMenuItem("��ʦ",false);
        JRadioButtonMenuItem mi_11 = new JRadioButtonMenuItem("�޵�",false);

        ButtonGroup group=new ButtonGroup();
        group.add(mi_6);
        group.add(mi_7);
        group.add(mi_8);
        group.add(mi_9);
        group.add(mi_10);
        group.add(mi_11);
        aboutme.addActionListener(menuItemAction);
        create.addActionListener(menuItemAction);
        save.addActionListener(menuItemAction);
        imports.addActionListener(menuItemAction);
        mi_6.addActionListener(menuItemAction);
        mi_7.addActionListener(menuItemAction);
        mi_8.addActionListener(menuItemAction);
        mi_9.addActionListener(menuItemAction);
        mi_10.addActionListener(menuItemAction);
        mi_11.addActionListener(menuItemAction);
        connect.addActionListener(menuItemAction);
        connectclient.addActionListener(menuItemAction);
        personalinfomation.addActionListener(menuItemAction);
        matchplayer.addActionListener(menuItemAction);
        exitserver.addActionListener(menuItemAction);
        seedimport.addActionListener(menuItemAction);
        seedexport.addActionListener(menuItemAction);
        importmanual.addActionListener(menuItemAction);
        exportmanual.addActionListener(menuItemAction);
        logwindow.addActionListener(menuItemAction);
        recordwindow.addActionListener(menuItemAction);
        endmanual.addActionListener(menuItemAction);
        exitthis.addActionListener(menuItemAction);
        aitoai.addActionListener(menuItemAction);

        create.setMnemonic(10);
        mi_6.setMnemonic(2);
        mi_7.setMnemonic(3);
        mi_8.setMnemonic(4);
        mi_9.setMnemonic(5);
        mi_10.setMnemonic(6);
        menu_file.setMnemonic('0');
        menu_file.add(create);
        menu_file.add(imports);
        menu_manual.add(importmanual);
        menu_manual.add(exportmanual);
        menu_manual.add(endmanual);
        menu_ai.add(mi_6);
        menu_ai.add(mi_7);
        menu_ai.add(mi_8);
        menu_ai.add(mi_9);
        menu_ai.add(mi_10);
        menu_ai.add(mi_11);
        menu_file.add(seedimport);
        menu_file.add(seedexport);
        menu_file.add(save);
        menu_else.add(aboutme);
        menu_else.add(exitthis);
        menu_net.add(connectclient);
        //menu_net.add(connect);
        //menu_net.add(personalinfomation);
        //menu_net.add(matchplayer);
        //menu_net.add(exitserver);
        jmb.add(menu_file);
        jmb.add(menu_ai);
        jmb.add(menu_manual);
        jmb.add(menu_net);

        //------------------------------------------------------
        JMenu menu_set = new JMenu("����");
        //JCheckBoxMenuItem redCmp = new JCheckBoxMenuItem("���Ժ췽",play!=REDPLAYSIGN);
        JCheckBoxMenuItem blackCmp = new JCheckBoxMenuItem("���Է��л�",play!=BLACKPLAYSIGN);
        JCheckBoxMenuItem doubles = new JCheckBoxMenuItem("˫�˶�ս",play!=BLACKPLAYSIGN&&play!=REDPLAYSIGN);
        ButtonGroup computer= new ButtonGroup();
        //computer.add(redCmp);
        computer.add(blackCmp);
        computer.add(doubles);

        JCheckBoxMenuItem isSoundBox= new JCheckBoxMenuItem("��Ч",isSound);


        ButtonGroup hashSizeGroup=new ButtonGroup();
        //hashSizeGroup.add(hashSize2M);
        //hashSizeGroup.add(hashSize32M);
        //hashSizeGroup.add(hashSize64M);


        JCheckBoxMenuItem backstageThink=new JCheckBoxMenuItem("��̨˼��",isBackstageThink);

        //redCmp.addActionListener(menuItemAction);
        blackCmp.addActionListener(menuItemAction);
        //hashSize2M.addActionListener(menuItemAction);
        //hashSize32M.addActionListener(menuItemAction);
        //hashSize64M.addActionListener(menuItemAction);
        backstageThink.addActionListener(menuItemAction);
        isSoundBox.addActionListener(menuItemAction);
        doubles.addActionListener(menuItemAction);

        menu_set.add(blackCmp);
        //menu_set.add(redCmp);
        menu_set.add(doubles);
        menu_set.add(aitoai);
        //menu_set.add(hashSize2M);
        //menu_set.add(hashSize32M);
        //menu_set.add(hashSize64M);
        menu_set.add(backstageThink);
        menu_set.add(isSoundBox);
        menu_set.add(logwindow);
        menu_set.add(recordwindow);
        jmb.add(menu_set);
        jmb.add(menu_else);
        noconnectedserver();
        return jmb;
    }
    public static void connectedserver(){
        matchplayer.setEnabled(true);
        personalinfomation.setEnabled(true);
        exitserver.setEnabled(true);
    }
    public static void noconnectedserver(){
        matchplayer.setEnabled(false);
        personalinfomation.setEnabled(false);
        exitserver.setEnabled(false);
    }
    public static void setBoardIconUnchecked(int site, int chess){
//		site=boardMap[site];
//		initBoardRelation(site,chess);
        if(chess==NOTHING){
            buttons[site].setIcon(null);
        }else{
            buttons[site].setIcon(getImageIcon(chessIcon[chess]));
        }
    }
    public void setBoardIconChecked(int site,int chess){
        buttons[site].setIcon(getImageIcon(chessIcon[chess]+"S"));
    }
    public void setCheckedLOSS(int play){
        buttons[chessParamCont.allChess[chessPlay[play]]].setIcon(getImageIcon(chessIcon[chessPlay[play]]+"M"));
    }
    public static void clearBoardIcon(){
        for(int i=0;i<buttons.length;i++){
            buttons[i].setIcon(null);
        }
    }
    public  void initBoardRelation(int destSite,int chess){

        chessParamCont.board[destSite]=chess;
        chessParamCont.allChess[chess]=destSite;

        int destRow = boardRow[destSite];
        int destCol = boardCol[destSite];
        chessParamCont.boardBitRow[destRow]|=(1<<(8-destCol));
        chessParamCont.boardBitCol[destCol]|=(1<<(9-destRow));

    }

    public void move(MoveNode moveNode){

        if(lastTimeCheckedSite!=-1){
            setBoardIconUnchecked(lastTimeCheckedSite,chessParamCont.board[lastTimeCheckedSite]);
        }
        setBoardIconUnchecked(moveNode.srcSite,NOTHING);
        setBoardIconChecked(moveNode.destSite,moveNode.srcChess);
        lastTimeCheckedSite=moveNode.destSite;
    }
    class ButtonActionListener   implements ActionListener, WindowListener,MouseListener {
        public void actionPerformed(ActionEvent e) {
            Button sour = (Button)e.getSource();
            if(sour.getLabel().equals("����")){
                int i = 0;
                while(i<2){
                    if(moveHistory.getMoveNode()!=null ){
                        MoveNode moveNode=moveHistory.getMoveNode();
                        unMoveNode(moveNode);
                        moveHistory=moveHistory.getLastLink();
                        turn_num--;
                        play=1-play;
                    }
                    i++;
                }
                addlog("Player: "+"����һ��");
            }else if(sour.getLabel().equals("AI��������")){
                if(_AIThink!=null){
                    _AIThink.setStop();
                }
            }else if(sour.getLabel().equals("����Ϸ")){
                disposeinn();
                clearmanuallist();
                new ChessBoardMain();
            }else if(sour.getLabel().equals("��һ��")){
                manualtime++;
                manualloadatime(manualtime);
                if (manualtime>=manuallistread.size()-1){
                    nextstep.setEnabled(false);
                }
                if (manualtime>0){
                    laststep.setEnabled(true);
                }
            }else if(sour.getLabel().equals("��һ��")){
                manualtime=manualtime-1;
                manualloadatime(manualtime);
                if (manualtime<manuallistread.size()){
                    nextstep.setEnabled(true);
                }
                if (manualtime<=0){
                    laststep.setEnabled(false);
                }

            }


        }
        private boolean checkZFPath(int srcSite,int destSite,int play){
            if(chessParamCont.board[srcSite]==NOTHING){
                return false;
            }
//			int row=chessParamCont.boardBitRow[boardRow[srcSite]];
//			int col=chessParamCont.boardBitCol[boardCol[srcSite]];
			/*BitBoard bt = BitBoard.assignXorToNew(GunBitBoardOfFakeAttackRow[srcSite][row],GunBitBoardOfFakeAttackCol[srcSite][col]);
			System.out.println(chessParamCont.maskBoardChesses);
			System.out.println("============??��??????��??==========");
			System.out.println(bt);*/
//			System.out.println("?????????????->>"+(ChariotAndGunMobilityRow[srcSite][row]+ChariotAndGunMobilityCol[srcSite][col]));

            MoveNode moveNode = new MoveNode(srcSite,destSite,chessParamCont.board[srcSite],chessParamCont.board[destSite],0);
            return cmp.legalMove(play,moveNode);
        }
        private void unMoveNode(MoveNode moveNode){
            MoveNode unmoveNode=new MoveNode();
            unmoveNode.srcChess=moveNode.destChess;
            unmoveNode.srcSite=moveNode.destSite;
            unmoveNode.destChess=moveNode.srcChess;
            unmoveNode.destSite=moveNode.srcSite;
            unMove(unmoveNode);
            cmp.unMoveOperate(moveNode);
        }
        private void unMove(MoveNode moveNode){
            if(lastTimeCheckedSite!=-1){
                setBoardIconUnchecked(lastTimeCheckedSite,chessParamCont.board[lastTimeCheckedSite]);
            }
            if(moveNode.srcChess==NOTHING){
                buttons[moveNode.srcSite].setIcon(null);
            }else{
                setBoardIconUnchecked(moveNode.srcSite,moveNode.srcChess);
            }
            if(moveNode.destChess==NOTHING){
                buttons[moveNode.destChess].setIcon(null);
            }else{
                setBoardIconChecked(moveNode.destSite,moveNode.destChess);
            }
            lastTimeCheckedSite=moveNode.destSite;
        }
        public void windowActivated(WindowEvent arg0) {
            // TODO Auto-generated method stub

        }

        public void windowClosed(WindowEvent arg0) {
            // TODO Auto-generated method stub

        }

        public void windowClosing(WindowEvent arg0) {
            // TODO Auto-generated method stub
            System.exit(0);
        }

        public void windowDeactivated(WindowEvent arg0) {
            // TODO Auto-generated method stub

        }

        public void windowDeiconified(WindowEvent arg0) {
            // TODO Auto-generated method stub

        }

        public void windowIconified(WindowEvent arg0) {
            // TODO Auto-generated method stub

        }

        public void windowOpened(WindowEvent arg0) {
            // TODO Auto-generated method stub

        }


        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        public void mousePressed(MouseEvent e) {
            String seed = null;
            if(android[play]){
                return;
            }
            for (int i = 0; i < buttons.length; i++) {
                JLabel p = buttons[i];
                if(p==e.getSource()){
                    if(chessParamCont.board[i]!=NOTHING &&  (chessParamCont.board[i]&chessPlay[play])==chessPlay[play]){//???????
                        if(i!=begin){
                            begin=i;

                            setBoardIconChecked(i,chessParamCont.board[i]);
                            if(lastTimeCheckedSite!=-1){
                                setBoardIconUnchecked(lastTimeCheckedSite,chessParamCont.board[lastTimeCheckedSite]);
                            }
                            lastTimeCheckedSite=begin;
                        }
                        return;
                    }else if(begin==-1){
                        return;
                    }
                    end=i;
                    if (this.checkZFPath(begin, end, play)) {
                        MoveNode moveNode = new MoveNode(begin, end, chessParamCont.board[begin], chessParamCont.board[end], 0);
                        try {
                            showMoveNode(moveNode);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        NodeLink nextLink = new NodeLink(play, transTable.boardZobrist32, transTable.boardZobrist64);
                        nextLink.setMoveNode(moveNode);
                        moveHistory.setNextLink(nextLink);
                        moveHistory = moveHistory.getNextLink();
                        begin = -1;
                        try {
                            opponentMove();
                            moved=1;
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
            /*
            new Thread(){
                public void run(){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(moved==1){
                        multgo();
                        moved=0;
                    }
                }
            }.start();

             */


        }

        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }
    }

    public static Integer moved=0;
    public static void multgo(){
        String seed = seedexport();
        addlog("Test: "+seed);
        try {
            Thread.sleep(100);
            astep();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException exc) {
            throw new RuntimeException(exc);
        }
    }
    public void gameOverMsg(String msg){
        if (JOptionPane.showConfirmDialog(this, msg, "��Ϸ���� �Ƿ������",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            disposeinn();
            clearmanuallist();
            new ChessBoardMain();
        }
    }
    public static Integer manualtime=0;
    public static void manualload(){
        manualstart();
        manualloadatime(manualtime);
    }

    public static void manualloadatime(Integer manualtimes){
        String manualFen = (String) Manual.manuallistread.get(manualtimes);
        String[] fenArray = Tools.fenToFENArray(manualFen);

        int[] boardTemp = Tools.parseFEN(fenArray[1]);
        //??????????????
        chessParamCont=ChessInitialize.getGlobalChessParam(boardTemp);
        //??????��?????
        clearBoardIcon();
        //???????????
        for(int i=0;i<boardTemp.length;i++){
            if(boardTemp[i]>0){
                ChessBoardMain.setBoardIconUnchecked(i,boardTemp[i]);
            }
        }

        transTable=new TranspositionTable() ;
    }

    public static void seedtoloadatime(String seed){
        String[] fenArray = Tools.fenToFENArray(seed);
        int[] boardTemp = Tools.parseFEN(fenArray[1]);
        //??????????????
        chessParamCont=ChessInitialize.getGlobalChessParam(boardTemp);
        //??????��?????
        clearBoardIcon();
        //???????????
        for(int i=0;i<boardTemp.length;i++){
            if(boardTemp[i]>0){
                setBoardIconUnchecked(i,boardTemp[i]);
            }
        }

        transTable=new TranspositionTable();
    }

    private static ImageIcon getImageIcon(String chessName){
        String path="/images/"+chessName+".GIF";
        ImageIcon  imageIcon=new  ImageIcon(ChessBoardMain.class.getResource(path));
        return imageIcon;
    }
    public static void GameEnd(){
        if(aitoai==1){
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("yyyy-MM-dd+HH:mm:ss");
            Date date = new Date();
            String filename = "aitoai-"+sdf.format(date);
            exportmanualevent(filename);
            if (JOptionPane.showConfirmDialog(null,"�����ѵ���Ϊ: "+filename+" ȷ�ϵ���?","��ʾ",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.YES_OPTION){
                importmanualevent(filename);
            }
            aitoai = 0;
        }
    }

    private boolean checkGameOver(){
        boolean isGameOver=false;
        String msg=null;
        if(moveHistory==null || moveHistory.getMoveNode()==null){
            msg="��ɱ";
            isGameOver=true;
            addlog("GameOver: "+"��ɱ");
        }else if(chessParamCont.allChess[chessPlay[BLACKPLAYSIGN]]==NOTHING || moveHistory.getMoveNode().destChess==chessPlay[BLACKPLAYSIGN]){
            isGameOver=true;
            msg="��ɱ";
            addlog("GameOver: "+"��ɱ");
        }else if(chessParamCont.allChess[chessPlay[REDPLAYSIGN]]==NOTHING || moveHistory.getMoveNode().destChess==chessPlay[REDPLAYSIGN]){
            msg="��ɱ";
            isGameOver=true;
            addlog("GameOver: "+"��ɱ");
        }else if(moveHistory.getMoveNode().score==-LONGCHECKSCORE){
            msg=(play==BLACKPLAYSIGN?"�ڷ�":"�췽")+"�����и���";
            addlog("GameOver: "+(play==BLACKPLAYSIGN?"�ڷ�":"�췽")+"�����и���");
            isGameOver=true;
        }else if(moveHistory.getMoveNode().score<=-(maxScore-2)){
            setCheckedLOSS(play);
            msg="��ɱ";
            isGameOver=true;
            addlog("GameOver: "+"��ɱ");
        }else if(moveHistory.getMoveNode().score>=(maxScore-2)){
            setCheckedLOSS(1-play);
            msg=(play==BLACKPLAYSIGN?"�ڷ�":"�췽")+"ʤ";
            isGameOver=true;
            addlog("GameOver: "+(play==BLACKPLAYSIGN?"�ڷ�":"�췽")+"ʤ");
        }else if(chessParamCont.getAttackChessesNum(REDPLAYSIGN)==0 && chessParamCont.getAttackChessesNum(BLACKPLAYSIGN)==0){
            msg="˫�����޹��������к�";
            addlog("GameOver: "+"˫�����޹��������к�");
            isGameOver=true;
        }else if(turn_num>=300){
            msg="�غ�������300�к�";
            addlog("GameOver: "+"�غ�������300�к�");
            isGameOver=true;
        }
        if(isGameOver){
            launchSound(SoundEffect.LOSS_SOUND);
            if (aitoai == 0){
                gameOverMsg(msg);
            }
            GameEnd();
        }else{
            MoveNode moveNode = moveHistory.getMoveNode();
            if(cmp.checked(1-play)){
                checktip();
                launchSound(SoundEffect.CHECKED_SOUND);
            }else if(moveNode.destChess!=NOTHING){
                launchSound(SoundEffect.CAPTURE_SOUND);
            }else{
                launchSound(SoundEffect.MOVE_SOUND);
            }
        }
        return isGameOver;
    }
    public void disposeinn(){
        dispose();
    }
    public static Integer aitoai = 0;
    public static void checktip(){
        JOptionPane.showMessageDialog(null,"����","��ʾ?",JOptionPane.PLAIN_MESSAGE);
    }
    class MenuItemActionListener extends JFrame  implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String actionCommand=e.getActionCommand();
            if("�½�".equals(actionCommand)){
                disposeinn();
                clearmanuallist();
                new ChessBoardMain();
            }else if("����".equalsIgnoreCase(actionCommand)){
                Tools.saveFEN(chessParamCont.board,moveHistory);
            }else if("����".equalsIgnoreCase(actionCommand)){
                startFen=readSaved();
                initHandler();
            }else if("���ӵ���".equalsIgnoreCase(actionCommand)){
                startFen=JOptionPane.showInputDialog(null,"����������:\n","���ӵ���",JOptionPane.PLAIN_MESSAGE);
                initHandler();
            }else if("���ӵ���".equalsIgnoreCase(actionCommand)){
                seedexp(chessParamCont.board,moveHistory);
            }else if("����".equals(actionCommand)){
                computerLevel=ComputerLevel.greenHand;
            }else if("����".equals(actionCommand)){
                computerLevel=ComputerLevel.introduction;
            }else if("ҵ��".equals(actionCommand)){
                computerLevel=ComputerLevel.amateur;
            }else if("ר��".equals(actionCommand)){
                computerLevel=ComputerLevel.career;
            }else if("��ʦ".equals(actionCommand)){
                computerLevel=ComputerLevel.master;
            }else if("�޵�".equals(actionCommand)){
                computerLevel=ComputerLevel.invincible;
            }else if("���Է��л�".equals(actionCommand)){
                android[BLACKPLAYSIGN]=!android[BLACKPLAYSIGN];
                if(android[BLACKPLAYSIGN] && (BLACKPLAYSIGN==play || turn_num<=0)){
                    if(turn_num<=0){
                        play=BLACKPLAYSIGN;
                        moveHistory.play=1-BLACKPLAYSIGN;
                    }
                    computeThinkStart();
                }
                android[REDPLAYSIGN]=!android[REDPLAYSIGN];
                if(android[REDPLAYSIGN] && (REDPLAYSIGN==play || turn_num<=0) ){
                    if(turn_num<=0){
                        play=REDPLAYSIGN;
                        moveHistory.play=1-REDPLAYSIGN;
                    }
                    computeThinkStart();
                }
            }else if("˫�˶�ս".equals(actionCommand)){
                android[BLACKPLAYSIGN]=!android[BLACKPLAYSIGN];
                if(turn_num<=0){
                    play=REDPLAYSIGN;
                    moveHistory.play=1-REDPLAYSIGN;
                }
            }else if("�ۿ�AI����".equals(actionCommand)){
                android[BLACKPLAYSIGN]=!android[BLACKPLAYSIGN];
                if(turn_num<=0){
                    play=REDPLAYSIGN;
                    moveHistory.play=1-REDPLAYSIGN;
                }
                android[BLACKPLAYSIGN]=!android[BLACKPLAYSIGN];
                if(android[BLACKPLAYSIGN] && (BLACKPLAYSIGN==play || turn_num<=0)){
                    if(turn_num<=0){
                        play=BLACKPLAYSIGN;
                        moveHistory.play=1-BLACKPLAYSIGN;
                    }
                    computeThinkStart();
                }
                android[REDPLAYSIGN]=!android[REDPLAYSIGN];
                if(android[REDPLAYSIGN] && (REDPLAYSIGN==play || turn_num<=0) ){
                    if(turn_num<=0){
                        play=REDPLAYSIGN;
                        moveHistory.play=1-REDPLAYSIGN;
                    }
                    computeThinkStart();
                }
                aitoai = 1;
            }else if("��̨˼��".equals(actionCommand)){
                isBackstageThink=!isBackstageThink;
                addlog("Player: "+"�л���̨˼��");
            }else if("��Ч".equals(actionCommand)){
                isSound=!isSound;
                addlog("Player: "+"�л���Ч");
            }else if("����������".equals(actionCommand)){
                //com.pj.chess.internetplay.Connection.Connection();
            }else if("��������".equals(actionCommand)){
                String filename = JOptionPane.showInputDialog(null,"�����������ļ���(�����׺):\n","����",JOptionPane.PLAIN_MESSAGE);
                importmanualevent(filename);
            }else if("��������".equals(actionCommand)){
                String filename = JOptionPane.showInputDialog(null,"����������:\n","����",JOptionPane.PLAIN_MESSAGE);
                exportmanualevent(filename);
            }else if("�˳�����".equals(actionCommand)){
                if (JOptionPane.showConfirmDialog(null,"ȷ���˳�����ģʽ","��ʾ",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.YES_OPTION){
                    manualend();
                    disposeinn();
                    new ChessBoardMain();
                }
            }else if("����־��¼����".equals(actionCommand)){
                com.pj.chess.LogWindow.logwindow();
            }else if("��AI�����������".equals(actionCommand)){
                com.pj.chess.RecordWindow.recordwindow();
            }else if("����".equals(actionCommand)){
                if (AlphaVersion){
                    JOptionPane.showMessageDialog(null, "�汾: " + alphaversion + "-alpha\n��������: " + alphaUpdateDate + "\n��������:\n" + alphaUpdatemessage
                            + "\n\nBug Fixes:\n" + alphaBugFixmessage +"\n\n���汾: " + version + "\n��������: " + Updatedate + "\n��������:\n" + Updatemessage
                            + "\n\nBug Fixes:\n" + BugFixmessage+ "\n\nGithub:\n" + Githublink + "\n������ʽ�汾:\n" + LatestReleases + "\nԤ�����汾:\n" + LatestTag + "\n" + AboutMessage, "����", JOptionPane.PLAIN_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "�汾: " + version + "\n��������: " + Updatedate + "\n��������:\n" + Updatemessage
                            + "\n\nBug Fixes:\n" + BugFixmessage + "\n\nGithub:\n" + Githublink + "\n������ʽ�汾:\n" + LatestReleases + "\nԤ�����汾:\n" + LatestTag + "\n" + "\n����ҳ��:\n" + publishpage + AboutMessage, "����", JOptionPane.PLAIN_MESSAGE);
                }
            }else if("�˳�".equals(actionCommand)){
                if (JOptionPane.showConfirmDialog(null,"ȷ���˳�?","��ʾ",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }else if("�˶Զ�ֱ��".equals(actionCommand)){
                handshaking();
            }else if("ƥ�����".equals(actionCommand)){
            }else if("������Ϣ".equals(actionCommand)){
            }else if("ȡ������".equals(actionCommand)){
            }
        }

    }

    public static void doubles(){
        android[BLACKPLAYSIGN]=!android[BLACKPLAYSIGN];
        if(turn_num<=0){
            play=REDPLAYSIGN;
            moveHistory.play=1-REDPLAYSIGN;
        }
    }

    private void opponentMove() throws IOException, InterruptedException {
        setHashTablesEnabled();
        Tools.getseed(chessParamCont.board,moveHistory);
        //??????????
        if(!checkGameOver()){
            turn_num++;
            play=1-play; //???????
            //????????????
            if(android[play]){
                computeThinkStart();
            }

        }
    }
    public static String seedexport(){
        addlog(String.valueOf(Tools.seedexporttime(chessParamCont.board,moveHistory)));
        return String.valueOf(Tools.seedexporttime(chessParamCont.board,moveHistory));
    }
    private void computeThinkStart(){
        jtextArea2.setText("");
        //???��?????
        if(isBackstageThink && (guessLink!=null && moveHistory!=null) ){
            //????????
            if(guessLink.getMoveNode().equals(moveHistory.getMoveNode())){
                new Thread(){
                    public void run(){
                        System.out.println("---->�²����У���");
                        addrlog("��̨˼��"+"---->�²����У���");
                        try {
                            //??????????
                            backstageAIThink.launchTimer();
                            backstageThinkThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            computeThink();
                        }
                        try {
                            computeAIMoving(guessLink.getNextLink());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.start();
            }else{
                new Thread(){
                    public void run(){
                        System.out.println("--->δ����");
                        addrlog("��̨˼��"+"--->δ����");
                        //?????��???????
                        backstageAIThink.setStop();
                        try {
                            backstageThinkThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("--->����˼��");
                        addrlog("��̨˼��"+"--->����˼��");
                        computeThink();
                    }
                }.start();
            }
        }else{
            computeThink();
        }
    }
    private  void computeThink(){
        new Thread(){
            public void run(){
                _AIThink.setLocalVariable(computerLevel,chessParamCont,moveHistory);
                _AIThink.launchTimer();
                _AIThink.run();
                try {
                    computeAIMoving(moveHistory.getNextLink());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    private void computeAIMoving(NodeLink nodeLink) throws IOException, InterruptedException {
        moveHistory = nodeLink;
        // if(!checkGameOver()){
        if (nodeLink != null && nodeLink.getMoveNode() != null) {
            MoveNode moveNode = nodeLink.getMoveNode();
            showMoveNode(moveNode);
        }
        opponentMove();
        backstageThink();
        // }
    }
    private Thread backstageThinkThread=null;
    private NodeLink guessLink ;
    //??????
    private void backstageThink(){
        if(!isBackstageThink){return;}
        if(moveHistory.getNextLink()!=null && moveHistory.getNextLink().getMoveNode()!=null){

            backstageThinkThread=new Thread(){
                public void run(){
                    //???????
                    guessLink = moveHistory.getNextLink();
                    backstageAIThink.setLocalVariable(computerLevel,chessParamCont,guessLink);
                    System.out.println("---->��ʼ�²�("+guessLink.getMoveNode()+")");
                    addrlog("---->��ʼ�²�("+guessLink.getMoveNode()+")");
                    backstageAIThink.guessRun(guessLink.getMoveNode());
                }
            };
            backstageThinkThread.start();
        }
    }
    private void showMoveNode(MoveNode moveNode) throws IOException, InterruptedException {
        if(moveNode!=null){
            move(moveNode);
            cmp.moveOperate(moveNode);
            transTable.synchroZobristBoardToStatic();
        }
        System.out.println();



    }
    private void setHashTablesEnabled(){
        //hashSize2M.setEnabled(false);
        //hashSize32M.setEnabled(false);
        //hashSize64M.setEnabled(false);
    }

    public  String readSaved(){
        String fen = null;
        FileInputStream fileInput = null;
        try {
            File chessFile = new File("chess.txt");
            fileInput = new java.io.FileInputStream(chessFile);
            BufferedReader bufferedReader = new BufferedReader(
                    new java.io.InputStreamReader(fileInput));

            while (bufferedReader.ready()) {
                fen = bufferedReader.readLine();
            }
            if (fen != null) {
                if (JOptionPane.showConfirmDialog(this, "��⵽�д浵�Ƿ�����ϴ���Ϸ?", "����",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    ObjectInputStream objInput =null;
                    try{
                        objInput = new ObjectInputStream(new FileInputStream("moves.dat"));
                        moveHistory=(NodeLink) objInput.readObject();
                        turn_num=20;
                    }catch(Exception e){
                        System.err.println("========��ȡ��ʷ��¼���� moves.dat");
                        addlogerror("========��ȡ��ʷ��¼���� moves.dat",1);
                        JOptionPane.showMessageDialog(null, "��ȡ��ʷ��¼����","����",JOptionPane.PLAIN_MESSAGE);
                    }finally{
                        if(objInput!=null){
                            objInput.close();
                        }
                    }
                } else {
                    chessFile.deleteOnExit();
                    JOptionPane.showMessageDialog(null, "û�м�⵽�浵����ȷ�ϴ浵��JAR������ͳһĿ¼��","����",JOptionPane.PLAIN_MESSAGE);
                    fen="c6c5  rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR b - - 0 1";
                }
            }
        } catch (Exception e) {
            fen="c6c5  rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR b - - 0 1";
        } finally {
            if(fileInput!=null){
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fen;
    }
    public static void main(String args[]) {
        if(Debugmode){
            com.pj.chess.LogWindow.logwindow();
            addlog("�ѿ���DebugMode");
        }
        versionagreed = readProperties("version");
        String message = "������־��Э��\n�汾: "+version+"\n��������: "+Updatedate+"\n\n���θ�������:\n "+Updatemessage+"\n\n�����޸�:\n"+BugFixmessage+"\n\n"+
                "�Ƿ�ͬ��:\nʹ�ñ�������ɵĺ����ʹ���߳е�?";
        String alphamessage = "������־��Э��\n�汾: "+alphaversion+"-alpha\n��������: "+alphaUpdateDate+"\n\n���θ�������:\n "+alphaUpdatemessage+"\n\n�����޸�:\n"+alphaBugFixmessage+
                "\n\n���汾: "+version+"\n��������: \n"+Updatedate+"\n\n���θ�������: \n"+Updatemessage+"\n�����޸�:\n"+BugFixmessage+"\n\n"+
                "�Ƿ�ͬ��:\nʹ�ñ�������ɵĺ����ʹ���߳е�?";
        if (AlphaVersion){
            if(!Objects.equals(versionagreed, alphaversion+"-alpha")){
                if(JOptionPane.showConfirmDialog(null,alphamessage,"NewVersion: "+alphaversion+"-alpha",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE)== JOptionPane.NO_OPTION){
                    System.exit(0);
                }
                writeProperties("version",alphaversion+"-alpha");
            }
            jtextArea.setText("");
            jtextArea2.setText("");
            new ChessBoardMain();
        }
        else{
        if(!Objects.equals(versionagreed, version)){
            if(JOptionPane.showConfirmDialog(null,message,"NewVersion: "+version,JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE)== JOptionPane.NO_OPTION){
                System.exit(0);
            }
            writeProperties("version",version);
        }
        jtextArea.setText("");
        jtextArea2.setText("");
        new ChessBoardMain();
        }
    }
    public void launchSound(int type){
        if(isSound){ //??????��
            new SoundEffect(type).start();
        }
    }
    private static final String movePathPath="/sounds/MOVE.WAV";
    private static final String checkedPath="/sounds/CHECKED.WAV";
    private static final String capturePath="/sounds/CAPTURE.WAV";
    private static final String lossPath="/sounds/LOSS.WAV";
    private static final URL MOVEPATHURL = ChessBoardMain.class.getResource(movePathPath);
    private static final URL CHECKEDURL = ChessBoardMain.class.getResource(checkedPath);
    private static final URL CAPTUREURL = ChessBoardMain.class.getResource(capturePath);
    private static final URL LOSSURL = ChessBoardMain.class.getResource(lossPath);
    class SoundEffect extends Thread{
        public final static int MOVE_SOUND=1;
        public final static int CAPTURE_SOUND=2;
        public final static int CHECKED_SOUND=3;
        public final static int LOSS_SOUND=4;

        URL url=null;

        public SoundEffect(int k){
            this.setDaemon(true);
            switch(k){
                case MOVE_SOUND:
                    url=MOVEPATHURL;
                    break;
                case CAPTURE_SOUND:
                    url=CAPTUREURL;
                    break;
                case CHECKED_SOUND:
                    url=CHECKEDURL;
                    break;
                case LOSS_SOUND:
                    url=LOSSURL;
                    break;
            }
        }
        public void run(){
            AudioClip  clip  =  Applet.newAudioClip(url);
            clip.play();
        }
    }
}