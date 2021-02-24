package dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MyH2Dialect extends H2Dialect { //내가 사용하는 Dialect를 상속받음.
   public MyH2Dialect(){
       registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
   }
}
