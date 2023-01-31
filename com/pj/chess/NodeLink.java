package com.pj.chess;

import static com.pj.chess.RecordWindow.addrlog;

import java.io.Serializable;

import com.pj.chess.chessmove.MoveNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**棋子着法链表
	 * @author pengjiu
	 */
	public class NodeLink implements Serializable{
		private NodeLink lastLink;
		private static final Logger LOG = LogManager.getLogger(NodeLink.class);
		private NodeLink nextLink;
		public int depth=0;
		private MoveNode moveNode;
		public boolean isNullMove;
		public long boardZobrist64; 
		public int boardZobrist32;
		public boolean chk=false; //是否将军
		//走棋方
		public int play;
		public boolean isQuiesc;
		public NodeLink(int play,int boardZobrist32,long boardZobrist64){
			
			//默认创建状态
			this(play,false,boardZobrist32,boardZobrist64);
		}
		public NodeLink(int play,boolean isNullMove,int boardZobrist32,long boardZobrist64){
			this.play=play;
			this.isNullMove=isNullMove;
			this.boardZobrist32=boardZobrist32;
			this.boardZobrist64=boardZobrist64;
		}
		public NodeLink(int play,MoveNode moveNode,int boardZobrist32,long boardZobrist64){
			this.play=play;
			this.moveNode=moveNode;
			this.boardZobrist32=boardZobrist32;
			this.boardZobrist64=boardZobrist64;
			isQuiesc=false;
		}
		public NodeLink(int play,MoveNode moveNode,int boardZobrist32,long boardZobrist64,boolean isQuiesc){
			this.play=play;
			this.moveNode=moveNode;
			this.boardZobrist32=boardZobrist32;
			this.boardZobrist64=boardZobrist64;
			this.isQuiesc=isQuiesc; 
		}
		public void setNextLink(NodeLink nextLink){
			this.nextLink=nextLink;
			if(nextLink!=null){
				nextLink.lastLink=this;
			}
//			nextLink.depth=this.depth+1;
		}
		public  NodeLink getNextLink(){
			return nextLink;
		}
		public boolean isNullMove(){
			return isNullMove;
		}
		public MoveNode getMoveNode() {
			return moveNode;
		}
		public void setMoveNode(MoveNode moveNode) {
			this.moveNode = moveNode;
		}
		public NodeLink getLastLink() {
			return lastLink;
		}
		public void setLastLink(NodeLink previousLink) {
			this.lastLink = previousLink;
			previousLink.nextLink=this;
			this.depth=previousLink.depth+1;
		}
		public void setLastLink(NodeLink previousLink,int depth) {
			this.lastLink = previousLink;
			previousLink.nextLink=this;
			this.depth=depth;
		}
		public String toString(){
			if(this==null){
				return "the NodeLink is NULL !";
			}
			StringBuilder sb=new StringBuilder();
			NodeLink firstLink = this;
//			while(firstLink.lastLink!=null){
//				firstLink=firstLink.lastLink;
//				
//			}
			NodeLink nextLink = firstLink;
			sb.append("==========================================================\n");
			LOG.info("==========================================================\n");
			addrlog("==========================================================");
			while(nextLink!=null){
					MoveNode movenode=nextLink.getMoveNode();
					sb.append(" 第->").append(nextLink.depth).append("步 ").append(movenode).append("\t"+(nextLink.isQuiesc?"静态搜索":"正常搜索")+"\t"+(nextLink.chk?"将军":"无将军")+"\n");
					LOG.info(" 第->" + nextLink.depth+"步 " + " " +(nextLink.isQuiesc?"静态搜索":"正常搜索")+(nextLink.chk?"将军":"无将军")+"\n");
					addrlog(" 第->" + nextLink.depth+"步 " + movenode + "" +(nextLink.isQuiesc?"静态搜索":"正常搜索")+"\t"+(nextLink.chk?"将军":"无将军")+"");
					nextLink=nextLink.getNextLink();
			}
			sb.append("==========================================================\n");
			LOG.info("==========================================================\n");
			addrlog("==========================================================");
			return sb.toString();
		}
	}
	
	
	
	
	
	
	
	
	
	
	