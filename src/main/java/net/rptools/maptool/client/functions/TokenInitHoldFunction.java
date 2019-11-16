/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.client.functions;

import java.math.BigDecimal;
import java.util.List;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolVariableResolver;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.InitiativeList.TokenInitiative;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.util.FunctionUtil;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.function.AbstractFunction;

/**
 * Set the token initiative hold value
 *
 * @author Jay
 */
public class TokenInitHoldFunction extends AbstractFunction {

  /** Getter has 0 to 2, setter has 1 to 3 */
  private TokenInitHoldFunction() {
    super(0, 3, "setInitiativeHold", "getInitiativeHold");
  }

  /** singleton instance of this function */
  private static final TokenInitHoldFunction singletonInstance = new TokenInitHoldFunction();

  /** @return singleton instance */
  public static TokenInitHoldFunction getInstance() {
    return singletonInstance;
  };

  @Override
  public Object childEvaluate(Parser parser, String functionName, List<Object> args)
      throws ParserException {
    MapToolVariableResolver res = (MapToolVariableResolver) parser.getVariableResolver();

    if (functionName.equalsIgnoreCase("getInitiativeHold")) {
      FunctionUtil.checkNumberParam(functionName, args, 0, 2);
      Token token = FunctionUtil.getTokenFromParam(res, functionName, args, 0, 1);
      return getInitiativeHold(token);
    } else {
      FunctionUtil.checkNumberParam(functionName, args, 1, 3);
      boolean set = FunctionUtil.paramAsBoolean(functionName, args, 0, true);
      Token token = FunctionUtil.getTokenFromParam(res, functionName, args, 1, 2);
      return setInitiativeHold(token, set);
    }
  }

  public static Object getInitiativeHold(Token token) {
    TokenInitiative ti = token.getInitiative();
    if (ti == null) return I18N.getText("macro.function.TokenInit.notOnList");
    return ti.isHolding() ? BigDecimal.ONE : BigDecimal.ZERO;
  }

  public static Object setInitiativeHold(Token token, boolean set) {
    if (token.getInitiatives().isEmpty())
      return I18N.getText("macro.function.TokenInit.notOnListSet");
    MapTool.serverCommand().updateTokenProperty(token, "setInitiativeHold", set);
    return set ? BigDecimal.ONE : BigDecimal.ZERO;
  }
}
