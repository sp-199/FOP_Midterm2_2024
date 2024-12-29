//comparing basic Ruby programming to java(helpful for our project as well as for general knowledge)

// puts - same as System.out.println();   print - same as System.out.print();

// in ruby u does not need to specify the type of the variable( age = 15 );also no ';' is necessary
// flaws = nil (means that it has no value)
// .strip(); - removes all unnecessary spaces from the String
// .include? "x" - tells us if our String includes x in it;
// float - basically same as double
// gets - equivalent of java's scanner(use gets.chomp() to print whole thing on 1 line)
// num.to_i - way of converting num into integer so that ruby does  not think that it is String
// E.G. - puts(num1 + num2) - ruby will print out values of these numbers in String so to_i method avoids that.
// def - way of defining a method(for example like public static void)
// example of method -  def sayhi(name)   - return keyword is used just as in java but nothing in a method after the return keyword is executed!
//                         puts ("Hello " + name)
//                      end

// calling a method - sayhi("Gigi")  //output - Hello Gigi

// basics of the if-else statements are almost same as in java with minor changes ->
//for example we use elsif here as(java - else if),also we say end at the end of the statements(as many of it as many else is in the code).
// example of while loop in Ruby -  while i >= 8
//                                       puts i
//                                     i+=1
//                                  end   (pretty much the same as while loops in java)

// to do something n times - n.times do
//                                 puts "coding is fun"
//                            end

// for loops in Ruby - E.G. - for x in 0..5
//                                puts x
//                            end
//or another way would be ->
//                      0..5.each do |y|
//                           puts y
//                       end
