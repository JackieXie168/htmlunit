/*
 * Copyright (c) 2015 Gargoyle Software Inc.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (http://www.gnu.org/licenses/).
 */
/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.gargoylesoftware.js.nashorn.tools;

import static com.gargoylesoftware.js.nashorn.internal.runtime.Source.sourceFor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.gargoylesoftware.js.nashorn.api.scripting.NashornException;
import com.gargoylesoftware.js.nashorn.internal.codegen.Compiler;
import com.gargoylesoftware.js.nashorn.internal.codegen.Compiler.CompilationPhases;
import com.gargoylesoftware.js.nashorn.internal.ir.Expression;
import com.gargoylesoftware.js.nashorn.internal.ir.FunctionNode;
import com.gargoylesoftware.js.nashorn.internal.ir.debug.ASTWriter;
import com.gargoylesoftware.js.nashorn.internal.ir.debug.PrintVisitor;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.parser.Parser;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.ErrorManager;
import com.gargoylesoftware.js.nashorn.internal.runtime.JSType;
import com.gargoylesoftware.js.nashorn.internal.runtime.Property;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptEnvironment;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;
import com.gargoylesoftware.js.nashorn.internal.runtime.options.Options;

/**
 * Command line Shell for processing JavaScript files.
 */
public class Shell implements PartialParser {

    /**
     * Resource name for properties file
     */
    private static final String MESSAGE_RESOURCE = "jdk.nashorn.tools.resources.Shell";
    /**
     * Shell message bundle.
     */
    protected static final ResourceBundle bundle = ResourceBundle.getBundle(MESSAGE_RESOURCE, Locale.getDefault());

    /**
     * Exit code for command line tool - successful
     */
    public static final int SUCCESS = 0;
    /**
     * Exit code for command line tool - error on command line
     */
    public static final int COMMANDLINE_ERROR = 100;
    /**
     * Exit code for command line tool - error compiling script
     */
    public static final int COMPILATION_ERROR = 101;
    /**
     * Exit code for command line tool - error during runtime
     */
    public static final int RUNTIME_ERROR = 102;
    /**
     * Exit code for command line tool - i/o error
     */
    public static final int IO_ERROR = 103;
    /**
     * Exit code for command line tool - internal error
     */
    public static final int INTERNAL_ERROR = 104;

    /**
     * Constructor
     */
    protected Shell() {
    }

    /**
     * Main entry point with the default input, output and error streams.
     *
     * @param args The command line arguments
     */
    public static void main(final String[] args) {
        try {
            final int exitCode = main(System.in, System.out, System.err, args);
            if (exitCode != SUCCESS) {
                System.exit(exitCode);
            }
        } catch (final IOException e) {
            System.err.println(e); //bootstrapping, Context.err may not exist
            System.exit(IO_ERROR);
        }
    }

    /**
     * Starting point for executing a {@code Shell}. Starts a shell with the
     * given arguments and streams and lets it run until exit.
     *
     * @param in input stream for Shell
     * @param out output stream for Shell
     * @param err error stream for Shell
     * @param args arguments to Shell
     *
     * @return exit code
     *
     * @throws IOException if there's a problem setting up the streams
     */
    public static int main(final InputStream in, final OutputStream out, final OutputStream err, final String[] args) throws IOException {
        return new Shell().run(in, out, err, args);
    }

    /**
     * Run method logic.
     *
     * @param in input stream for Shell
     * @param out output stream for Shell
     * @param err error stream for Shell
     * @param args arguments to Shell
     *
     * @return exit code
     *
     * @throws IOException if there's a problem setting up the streams
     */
    protected final int run(final InputStream in, final OutputStream out, final OutputStream err, final String[] args) throws IOException {
        final Context context = makeContext(in, out, err, args);
        if (context == null) {
            return COMMANDLINE_ERROR;
        }

        final Global global = context.createGlobal();
        final ScriptEnvironment env = context.getEnv();
        final List<String> files = env.getFiles();
        if (files.isEmpty()) {
            return readEvalPrint(context, global);
        }

        if (env._compile_only) {
            return compileScripts(context, global, files);
        }

        if (env._fx) {
            return runFXScripts(context, global, files);
        }

        return runScripts(context, global, files);
    }

    /**
     * Make a new Nashorn Context to compile and/or run JavaScript files.
     *
     * @param in input stream for Shell
     * @param out output stream for Shell
     * @param err error stream for Shell
     * @param args arguments to Shell
     *
     * @return null if there are problems with option parsing.
     */
    private static Context makeContext(final InputStream in, final OutputStream out, final OutputStream err, final String[] args) {
        final PrintStream pout = out instanceof PrintStream ? (PrintStream) out : new PrintStream(out);
        final PrintStream perr = err instanceof PrintStream ? (PrintStream) err : new PrintStream(err);
        final PrintWriter wout = new PrintWriter(pout, true);
        final PrintWriter werr = new PrintWriter(perr, true);

        // Set up error handler.
        final ErrorManager errors = new ErrorManager(werr);
        // Set up options.
        final Options options = new Options("nashorn", werr);

        // parse options
        if (args != null) {
            try {
                options.process(args);
            } catch (final IllegalArgumentException e) {
                werr.println(bundle.getString("shell.usage"));
                options.displayHelp(e);
                return null;
            }
        }

        // detect scripting mode by any source's first character being '#'
        if (!options.getBoolean("scripting")) {
            for (final String fileName : options.getFiles()) {
                final File firstFile = new File(fileName);
                if (firstFile.isFile()) {
                    try (final FileReader fr = new FileReader(firstFile)) {
                        final int firstChar = fr.read();
                        // starts with '#
                        if (firstChar == '#') {
                            options.set("scripting", true);
                            break;
                        }
                    } catch (final IOException e) {
                        // ignore this. File IO errors will be reported later anyway
                    }
                }
            }
        }

        return new Context(options, errors, wout, werr, Thread.currentThread().getContextClassLoader());
    }

    /**
     * Compiles the given script files in the command line
     * This is called only when using the --compile-only flag
     *
     * @param context the nashorn context
     * @param global the global scope
     * @param files the list of script files to compile
     *
     * @return error code
     * @throws IOException when any script file read results in I/O error
     */
    private static int compileScripts(final Context context, final Global global, final List<String> files) throws IOException {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = (oldGlobal != global);
        final ScriptEnvironment env = context.getEnv();
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            final ErrorManager errors = context.getErrorManager();

            // For each file on the command line.
            for (final String fileName : files) {
                final FunctionNode functionNode = new Parser(env, sourceFor(fileName, new File(fileName)), errors, env._strict, 0, context.getLogger(Parser.class)).parse();

                if (errors.getNumberOfErrors() != 0) {
                    return COMPILATION_ERROR;
                }

                Compiler.forNoInstallerCompilation(
                       context,
                       functionNode.getSource(),
                       env._strict | functionNode.isStrict()).
                       compile(functionNode, CompilationPhases.COMPILE_ALL_NO_INSTALL);

                if (env._print_ast) {
                    context.getErr().println(new ASTWriter(functionNode));
                }

                if (env._print_parse) {
                    context.getErr().println(new PrintVisitor(functionNode));
                }

                if (errors.getNumberOfErrors() != 0) {
                    return COMPILATION_ERROR;
                }
            }
        } finally {
            env.getOut().flush();
            env.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }

        return SUCCESS;
    }

    /**
     * Runs the given JavaScript files in the command line
     *
     * @param context the nashorn context
     * @param global the global scope
     * @param files the list of script files to run
     *
     * @return error code
     * @throws IOException when any script file read results in I/O error
     */
    private int runScripts(final Context context, final Global global, final List<String> files) throws IOException {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = (oldGlobal != global);
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }
            final ErrorManager errors = context.getErrorManager();

            // For each file on the command line.
            for (final String fileName : files) {
                if ("-".equals(fileName)) {
                    final int res = readEvalPrint(context, global);
                    if (res != SUCCESS) {
                        return res;
                    }
                    continue;
                }

                final File file = new File(fileName);
                final ScriptFunction script = context.compileScript(sourceFor(fileName, file), global);
                if (script == null || errors.getNumberOfErrors() != 0) {
                    return COMPILATION_ERROR;
                }

                try {
                    apply(script, global);
                } catch (final NashornException e) {
                    errors.error(e.toString());
                    if (context.getEnv()._dump_on_error) {
                        e.printStackTrace(context.getErr());
                    }

                    return RUNTIME_ERROR;
                }
            }
        } finally {
            context.getOut().flush();
            context.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }

        return SUCCESS;
    }

    /**
     * Runs launches "fx:bootstrap.js" with the given JavaScript files provided
     * as arguments.
     *
     * @param context the nashorn context
     * @param global the global scope
     * @param files the list of script files to provide
     *
     * @return error code
     * @throws IOException when any script file read results in I/O error
     */
    private static int runFXScripts(final Context context, final Global global, final List<String> files) throws IOException {
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = (oldGlobal != global);
        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }

            global.addOwnProperty("$GLOBAL", Property.NOT_ENUMERABLE, global);
            global.addOwnProperty("$SCRIPTS", Property.NOT_ENUMERABLE, files);
            context.load(global, "fx:bootstrap.js");
        } catch (final NashornException e) {
            context.getErrorManager().error(e.toString());
            if (context.getEnv()._dump_on_error) {
                e.printStackTrace(context.getErr());
            }

            return RUNTIME_ERROR;
        } finally {
            context.getOut().flush();
            context.getErr().flush();
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }

        return SUCCESS;
    }

    /**
     * Hook to ScriptFunction "apply". A performance metering shell may
     * introduce enter/exit timing here.
     *
     * @param target target function for apply
     * @param self self reference for apply
     *
     * @return result of the function apply
     */
    protected Object apply(final ScriptFunction target, final Object self) {
        return ScriptRuntime.apply(target, self);
    }

    /**
     * Parse potentially partial code and keep track of the start of last expression.
     * This 'partial' parsing support is meant to be used for code-completion.
     *
     * @param context the nashorn context
     * @param code code that is to be parsed
     * @return the start index of the last expression parsed in the (incomplete) code.
     */
    @Override
    public final int getLastExpressionStart(final Context context, final String code) {
        final int[] exprStart = { -1 };

        final Parser p = new Parser(context.getEnv(), sourceFor("<partial_code>", code),new Context.ThrowErrorManager()) {
            @Override
            protected Expression expression() {
                exprStart[0] = this.start;
                return super.expression();
            }

            @Override
            protected Expression assignmentExpression(final boolean noIn) {
                exprStart[0] = this.start;
                return super.expression();
            }
        };

        try {
            p.parse();
        } catch (final Exception ignored) {
            // throw any parser exception, but we are partial parsing anyway
        }

        return exprStart[0];
    }


    /**
     * read-eval-print loop for Nashorn shell.
     *
     * @param context the nashorn context
     * @param global  global scope object to use
     * @return return code
     */
    protected int readEvalPrint(final Context context, final Global global) {
        final String prompt = bundle.getString("shell.prompt");
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final PrintWriter err = context.getErr();
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = (oldGlobal != global);
        final ScriptEnvironment env = context.getEnv();

        try {
            if (globalChanged) {
                Context.setGlobal(global);
            }

            global.addShellBuiltins();

            while (true) {
                err.print(prompt);
                err.flush();

                String source = "";
                try {
                    source = in.readLine();
                } catch (final IOException ioe) {
                    err.println(ioe.toString());
                }

                if (source == null) {
                    break;
                }

                if (source.isEmpty()) {
                    continue;
                }

                try {
                    final Object res = context.eval(global, source, global, "<shell>");
                    if (res != ScriptRuntime.UNDEFINED) {
                        err.println(JSType.toString(res));
                    }
                } catch (final Exception e) {
                    err.println(e);
                    if (env._dump_on_error) {
                        e.printStackTrace(err);
                    }
                }
            }
        } finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }

        return SUCCESS;
    }
}