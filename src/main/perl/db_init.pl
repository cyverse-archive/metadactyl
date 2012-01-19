#!/usr/bin/perl

use warnings;
use strict;

use lib 'lib';

use Carp;
use English qw( -no_match_vars );
use File::Basename;
use Getopt::Long;

use PostgreSQL;
use MySQL;

use constant VERSION => '0.0.1';

# The default values for the command-line options.
my $host     = 'localhost';
my $sql_dir  = '.';
my $user     = 'de';
my $database = 'workflow';
my $dbms     = 'MySQL';

# Allow command-line option bundling.
Getopt::Long::Configure("bundling_override");

# Get the command-line options.
my $opts_ok = GetOptions(
    'host|h=s'     => \$host,
    'sql-dir|s=s'  => \$sql_dir,
    'user|u=s'     => \$user,
    'database|d=s' => \$database,
    'mysql|m'      => sub { $dbms = 'MySQL' },
    'postgres|p'   => sub { $dbms = 'PostgreSQL' },
    'usage'        => sub { display_usage(); exit },
    'help'         => sub { display_help(); exit },
    'version'      => sub { display_version(); exit },
    'man'          => sub { display_manpage(); exit },
);

# The list of database initialization scripts.
my @sql_scripts
    = qw( workflow-0.sql property-types-0.sql rule-types-0.sql reference-genome-0.sql samtools-0.sql );

# Initialize the database.
my $initializer = $dbms->new(
    {   'host'     => $host,
        'sql_dir'  => $sql_dir,
        'user'     => $user,
        'database' => $database,
        'scripts'  => \@sql_scripts,
    }
);
$initializer->initialize();

exit;

##
# Usage      : display_usage();
#
# Purpose    : Displays a brief usage message.
#
# Returns    : Nothing.
#
# Parameters : None.
#
# Throws     : No exceptions.
sub display_usage {
    my $prog = basename $0;
    print <<"END_OF_USAGE";
Usage:
  $prog [--host=host] [--sql-dir=dir] [--user=user] [--database=database]
        [ --mysql | --postgres]
  $prog [-h host] [-s dir] [-u user] [-d database] [-m | -p]
  $prog --usage
  $prog --help
  $prog --version
  $prog --man
END_OF_USAGE
    return;
}

##
# Usage      : display_help();
#
# Purpose    : Displays the usage message along with brief descriptions of all
#              of the available command-line options.
#
# Returns    : Nothing.
#
# Parameters : None.
#
# Throws     : No exceptions.
sub display_help {
    display_usage();
    print <<"END_OF_HELP";

Options:
  --host     -h - the name of the machine hosting the database.
  --sql-dir  -s - the directory containing the sql initialization script.
  --user     -u - the username to use when logging into the database.
  --database -d - the name of the database.
  --mysql    -m - indicates that the DBMS in use is MySQL.
  --postgres -p - indicates that the DBMS in use is Postgres.
  --usage       - displays a brief usage message.
  --help        - displays this help text.
  --version     - displays the version of this utility.
  --man         - displays the manual page for this utility.
END_OF_HELP
    return;
}

##
# Usage      : display_version();
#
# Purpose    : Displays the verskion number for this utility.
#
# Returns    : Nothing.
#
# Parameters : None.
#
# Throws     : No exceptions.
sub display_version {
    print VERSION . "\n";
    return;
}

##
# Usage      : display_manpage();
#
# Purpose    : Displays the manual page for this utility.
#
# Returns    : Nothing.
#
# Parameters : None.
#
# Throws     : No exceptions.
sub display_manpage {
    local %ENV = %ENV;
    $ENV{LESS} = '-rf';
    system( "perldoc", $0 );
    return;
}

1;
__END__

=head1 NAME

db_init.pl - initializes the user authentication database.

=head1 VERSION

This documentation refers to db_init.pl version 0.0.1.

=head1 USAGE

  db_init.pl [--host=host] [--sql-dir=dir] [--user=user] [--database=database]
             [ --mysql | --postgres]
  db_init.pl [-h host] [-s dir] [-u user] [-d database] [-m | -p]

  # Display a brief usage message.
  db_init.pl --usage

  # Display the usage message plus descriptions of the command-line options.
  db_init.pl --help
  
  # Display 
  db_init.pl --version
  db_init.pl --man

  # Initialize a database using the default options.
  db_init.pl

  # Use Postgres instead of MySQL.
  db_init.pl --postgres
  db_init.pl -p
  
  # Explicitly specify MySQL.
  db_init.pl --mysql
  db_init.pl -m
  
  # Explicitly specify the host, SQL script location, user and database.
  db_init.pl --host=somehost --sql-dir=foo --user=bar --database=baz
  db_init.pl -h somehost -s foo -u bar -d baz

=head1 OPTIONS

=over 2

=item --host -h

The name of the machine that is hosting the database.  The default is
'localhost'.

=item --sql-dir -s

The directory containing the SQL initialization scripts.  The default is the
current working directory.

=item --user -u

The username to use when logging into the database.  The default is 'de'.
Note that this option is ignored when the database management system is MySQL.
The reason for this is that MySQL only allows one username and password to be
defined per configuration file, so the username is always obtained from the
configuration file.

=item --database -d

The name of the database to use.  The default is 'workflow'.

=item --mysql -m

Indicates that MySQL is being used.

=item --postgres -P

Indicates that PostgreSQL is being used.

=item --usage

Displays a brief usage message.

=item --help

Displays the usage message along with descriptions of the command-line
options.

=item --version

Displays the version number of this utility.

=item --man

Displays the manual page for this utility.

=back

=head1 DESCRIPTION

Creates the database and the schema used by the identity provider for the
iPlant discovery environment.

=head2 Prerequisites

=over 2

=item *

The discovery environment user database exists and has the selected name.
Recall that the database name defaults to 'workflow' if no database is
specified on the command line.  Also, the selected user must have full
privileges on this database.  Note that the username argument is ignored.

=item *

The selected user must exist and have full privileges for the discovery
environment user database.  If the user name isn't specified on the command
line then it defaults to 'de'.  Note that the username option is ignored when
the database management system is MySQL, in which case the username is
obtained from the MySQL configuration file.

=item *

For PostgreSQL, a password file, .pgpass, must exist in the user's home
directory and must contain a specification for the selected username and
database.

=item *

For MySQL, a configuraiton file, .my.cnf, must exist in the user's home
directory and must specify a username and password.

=back

=head2 Initializing a Database

To initialize a database, you can run this command:

  perl db_init.pl

This command drops the database if it already exists and creates a fresh copy
of the database.  The database schema is created by executing one or more SQL
scripts, which are defined in an array, @sql_scripts.  When applicable, the
owner of the database is changed to the selected user (or the default user if
a uername isn't explicitly specified).

=head2 Adding a New SQL Script.

To add a new SQL script to the list of SQL scripts, define the script and add
its name to the array @sql_scripts in db_init.pl.

=head1 DIAGNOSTICS

=over 2

=item <description>, <file>, not found

A required file was not found in the local file system.  Verify that the file
name is correct and that the file exists.

=item unable to execute <command>, <reason>

One of the commands that were issued by the script could not be executed.
Verify that the selected database management system is correct (MySQL is the
default), that the programs for the selected database management system are
in the user's path and that the user has permission to execute these programs.

=item unable to execute <command>

This is very similar to the previous error message except that the program
encountered an error during execution.  Verify that all of the prerequisites
listed above have been met.

=item unable to clear the database: <reason>

The utility was unable to clear the database.  This error will usually appear
in conjunction with one of the errors listed above.  Verify that all of the
prerequisites listed above have been met.

=item unable to change the database ownership: <reason>

The utility was unable to change the ownership of the database.  This error
will usually appear in conjunction with one of the errors listed above.
Verify that all of the prerequisites listed above have been met.

=item unable to run the database scripts: <reason>

An error was encountered when the utility was trying to run one of the
database initialization scripts.  This error will usually appear in
conjunction with one of the errors listed above.  Verify that all of the
prerequisites listed above have been met.

=item unable to find <file>

One of the database initialization script files could not be found.  Verify
that the file exists.

=back

=head1 DEPENDENCIES

=over 2

=item Carp

Used to display error messages with stack traces.  This module is part of the
standard Perl distribution.

=item English

Used to provide readable names for Perl punctuation characters.  This module
is part of the standard Perl distribution.

=item File::Basename

Used to obtain the name of this script without the full path.  This module is
part of the standard Perl distribution.

=item Getopt::Long

Used to parse the command-line options.  This module is part of the standard
Perl distribution.

=head1 BUGS AND LIMITATIONS

=over 2

=item *

The --user option (also known as -u) is ignored when the database management
system is MySQL.  The reason for this is that MySQL only allows one username
and password to be specified per host in the configuration file.  Rather than
attempt to come up with a workaround specifying the username and password on
the command line (and thus risk allowing someone to discover the password by
executing a simple 'ps' command), we decided to always use whichever user is
specified in the MySQL configuration file.

=back

=cut
