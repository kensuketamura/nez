package nez.expr;

import nez.SourceContext;
import nez.ast.SourcePosition;
import nez.util.UList;
import nez.util.UMap;
import nez.vm.Instruction;
import nez.vm.Compiler;

public class Match extends Unary {
	Match(SourcePosition s, Expression inner) {
		super(s, inner);
	}
	@Override
	public String getPredicate() { 
		return "~";
	}
	@Override
	public String getInterningKey() { 
		return "~";
	}	
	@Override
	Expression dupUnary(Expression e) {
		return (this.inner != e) ? Factory.newMatch(this.s, e) : this;
	}
	@Override
	public boolean checkAlwaysConsumed(GrammarChecker checker, String startNonTerminal, UList<String> stack) {
		return this.inner.checkAlwaysConsumed(checker, startNonTerminal, stack);
	}
	@Override
	public int inferNodeTransition(UMap<String> visited) {
		return NodeTransition.BooleanType;
	}
	@Override
	public Expression checkNodeTransition(GrammarChecker checker, NodeTransition c) {
		return this.inner.removeNodeOperator();
	}
	@Override
	public short acceptByte(int ch) {
		return this.inner.acceptByte(ch);
	}
	@Override
	public boolean match(SourceContext context) {
		return this.inner.matcher.match(context);
	}
	
	@Override
	public Instruction encode(Compiler bc, Instruction next) {
		return this.inner.encode(bc, next);
	}

	@Override
	protected int pattern(GEP gep) {
		return inner.pattern(gep);
	}

	@Override
	protected void examplfy(GEP gep, StringBuilder sb, int p) {
		this.inner.examplfy(gep, sb, p);
	}

}