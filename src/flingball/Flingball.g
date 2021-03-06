/* Copyright (c) 2017 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

@skip whitespace {
	game ::= COMMENT* board (COMMENT | ball | gadget | interaction)*;
	board::= 'board' 'name''=' NAME gravity? friction1? friction2?;
	gadget::= circle | triangle | absorber | square;
	square::= 'squareBumper' 'name''='NAME 'x''='INTEGER 'y''='INTEGER;
	circle::= 'circleBumper' 'name''='NAME 'x''='INTEGER 'y''='INTEGER;
	triangle::= 'triangleBumper' 'name''='NAME 'x''='INTEGER 'y''='INTEGER ('orientation''=' ANGLE)?;
	absorber::= 'absorber' 'name''='NAME 'x''='INTEGER 'y''='INTEGER 'width''='INTEGER 'height''='INTEGER;
	interaction::= 'fire' 'trigger''='NAME 'action''='NAME;
	ball::= 'ball name='NAME 'x='FLOAT 'y='FLOAT 'xVelocity='FLOAT 'yVelocity='FLOAT;
	
	gravity::= 'gravity''=' FLOAT;
	friction1::= 'friction1''=' FLOAT;
	friction2::= 'friction2''=' FLOAT;
}

whitespace ::= [ \t\n\r]+;
INTEGER ::= [0-9]+;
NAME ::= [A-Za-z_][A-Za-z_0-9]*;
FLOAT ::= '-'?([0-9]+.[0-9]*|.?[0-9]+);
ANGLE ::= '0'|'90'|'180'|'270';
COMMENT::= '#'[^\n]*'\n';
