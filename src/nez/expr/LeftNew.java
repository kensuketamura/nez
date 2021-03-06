package nez.expr;

import java.util.TreeMap;

import nez.SourceContext;
import nez.ast.Node;
import nez.ast.SourcePosition;
import nez.util.UList;
import nez.vm.Compiler;
import nez.vm.Instruction;

public class LeftNew extends New {
	LeftNew(SourcePosition s, UList<Expression> list) {
		super(s, list);
	}
	@Override
	public String getPredicate() { 
		return "leftnew";
	}
	@Override
	public String getInterningKey() {
		return "{@}";
	}
	@Override
	public Expression checkNodeTransition(GrammarChecker checker, NodeTransition c) {
		if(c.required != NodeTransition.OperationType) {
			checker.reportWarning(s, "unexpected {@ .. => removed!!");
			return this.removeNodeOperator();
		}
		c.required = NodeTransition.OperationType;
		for(Expression p: this) {
			p.checkNodeTransition(checker, c);
		}
		return this;
	}
	@Override
	public Expression removeFlag(TreeMap<String, String> undefedFlags) {
		UList<Expression> l = new UList<Expression>(new Expression[this.size()]);
		for(int i = 0; i < this.size(); i++) {
			Expression e = get(i).removeFlag(undefedFlags);
			Factory.addSequence(l, e);
		}
		return Factory.newNewLeftLink(this.s, l);
	}
	@Override
	public boolean match(SourceContext context) {
		long startIndex = context.getPosition();
//		ParsingObject left = context.left;
		for(int i = 0; i < this.prefetchIndex; i++) {
			if(!this.get(i).matcher.match(context)) {
				context.rollback(startIndex);
				return false;
			}
		}
		int mark = context.startConstruction();
		Node newnode = context.newNode();
		context.lazyJoin(context.left);
		context.lazyLink(newnode, 0, context.left);
		context.left = newnode;
		for(int i = 0; i < this.size(); i++) {
			if(!this.get(i).matcher.match(context)) {
				context.abortConstruction(mark);
				context.rollback(startIndex);
				newnode = null;
				return false;
			}
		}
		newnode.setEndingPosition(context.getPosition());
		//context.commitLinkLog2(newnode, startIndex, mark);
		//System.out.println("newnode: " + newnode.oid);
		context.left = newnode;
		newnode = null;
		return true;
	}
	
	@Override
	public Instruction encode(Compiler bc, Instruction next) {
		return bc.encodeLeftNew(this, next);
	}

}