package artlu;

import java.util.List;
import java.util.Map;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

public class ArtluParser {
	private static final String ARTLU_CORE = "artlu.core";
	private static final String CLOJURE_CORE = "clojure.core";
	private static final String INSTAPARSE_CORE= "instaparse.core";

	private final IFn require;
	private final IFn parse;
	private final IFn isFailure;
	private final IFn getFailure;
	private IFn decode;

	public ArtluParser() {
		require = Clojure.var(CLOJURE_CORE, "require");
		require.invoke(Clojure.read(ARTLU_CORE));
		parse = Clojure.var(ARTLU_CORE, "parse");
		isFailure = Clojure.var(INSTAPARSE_CORE, "failure?");
		getFailure = Clojure.var(ARTLU_CORE, "get-failure");
		decode = Clojure.var(ARTLU_CORE, "decode");
	}
	
	public final Object parse(final String text) {
		return parse.invoke(text);
	}
	public boolean isFailure(final Object ast) {
		return (boolean) isFailure.invoke(ast);
	}
	
	public Object getFailure(final Object ast) {
		return getFailure.invoke(ast);
	}
	
	public IFn decode(Object ast, String decoder) {
		return  (IFn) decode.invoke(ast, decoder);
	}
	
	public static final List<Map<String, Object>> decode(IFn decodeFn, byte[] data) {
		return (List<Map<String, Object>>) decodeFn.invoke(data);
	}
}
