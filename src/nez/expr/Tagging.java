package nez.expr;

import nez.SourceContext;
import nez.ast.SourcePosition;
import nez.ast.Tag;
import nez.util.StringUtils;
import nez.util.UMap;
import nez.vm.Compiler;
import nez.vm.Instruction;

public class Tagging extends Unconsumed {
	public Tag tag;
	Tagging(SourcePosition s, Tag tag) {
		super(s);
		this.tag = tag;
	}
	Tagging(SourcePosition s, String name) {
		this(s, Tag.tag(name));
	}
	@Override
	public String getPredicate() {
		return "tag " + StringUtils.quoteString('"', tag.name, '"');
	}
	@Override
	public String getInterningKey() {
		return "#" + this.tag.toString();
	}
	@Override
	public int inferNodeTransition(UMap<String> visited) {
		return NodeTransition.OperationType;
	}
	@Override
	public Expression checkNodeTransition(GrammarChecker checker, NodeTransition c) {
		if(c.required != NodeTransition.OperationType) {
			checker.reportWarning(s, "unexpected #" + tag.toString() + " => removed!!");
			return Factory.newEmpty(this.s);
		}
		return this;
	}
	@Override
	public Expression removeNodeOperator() {
		return Factory.newEmpty(null);
	}
	
	@Override
	public boolean match(SourceContext context) {
		context.left.setTag(this.tag);
		return true;
	}
	
	@Override
	public Instruction encode(Compiler bc, Instruction next) {
		return bc.encodeTagging(this, next);
	}

	@Override
	protected int pattern(GEP gep) {
		return 0;
	}

	@Override
	protected void examplfy(GEP gep, StringBuilder sb, int p) {
	}

}