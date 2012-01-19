package MySQL;

use warnings;
use strict;

use Database;

use base qw(Database);

use Carp;
use English qw(-no_match_vars);

##
# Provides the basic methods required to be able to initialize a MySQL
# database.
{
    ##
    # Usage      : $obj->validate_environment();
    #
    # Purpose    : Verifies that the environment includes everything required
    #              for this utility to be able to initialize a MySQL database.
    #
    # Returns    : Nothing.
    #
    # Parameters : None.
    #
    # Throws     : Any exception thrown by subroutines that this one calls.
    sub validate_environment {
        my ($self) = @_;
        $self->verify_file_existence(
            { "$ENV{HOME}/.my.cnf" => "MySQL configuration file", } );
        return;
    }

    ##
    # Usage      : $obj->clear_database();
    #
    # Purpose    : Clears the existing database by dropping all tables in the
    #              database.
    #
    # Returns    : Nothing.
    #
    # Parameters : None.
    #
    # Throws     : "unable to clear the database: $reason"
    sub clear_database {
        my ($self) = @_;
        eval {
            my @lines = $self->_execute_sql("SHOW TABLES");
            $self->_remove_table_header( \@lines );
            for my $table (@lines) {
                chomp $table;
                $self->_execute_sql("DROP TABLE $table");
            }
        };
        croak "unable to clear the database: $EVAL_ERROR"
            if $EVAL_ERROR;
        return;
    }

    ##
    # Usage      : $obj->change_database_ownership();
    #
    # Purpose    : Changes the ownership of the database to the selected user
    #              if applicable.
    #
    # Returns    : Nothing.
    #
    # Parameters : None.
    #
    # Throws     : No exceptions.
    #
    # Comments   : This subroutine doesn't apply to MySQL.
    sub change_database_ownership {
        my ($self) = @_;
        return;
    }

    ##
    # Usage      : $obj->run_database_scripts();
    #
    # Purpose    : Runs the database initialization scripts.
    #
    # Returns    : Nothing.
    #
    # Parameters : None.
    #
    # Throws     : "unable to run the database scripts: $reason"
    sub run_database_scripts {
        my ($self) = @_;
        eval {
            for my $script ( @{ $self->get_scripts() } ) {
                $self->_execute_sql_file($script);
            }
        };
        croak "unable to run the database scripts: $EVAL_ERROR"
            if $EVAL_ERROR;
        return;
    }

    ##
    # Usage      : $obj->_execute_sql_file($file);
    #
    # Purpose    : Executes a single database initialization script.
    #
    # Returns    : Nothing.
    #
    # Parameters : $file - the path to the file containing the script.
    #
    # Throws     : Any exception thrown by subroutines that this one calls.
    sub _execute_sql_file {
        my ( $self, $file ) = @_;
        croak "unable to find $file"
            if !-e $file;
        $self->_execute_sql("source $file");
        return;
    }

    ##
    # Usage      : @results = $obj->_execute_sql($sql);
    #
    # Purpose    : Executes an SQL statement and returns the output as an
    #              array of lines.
    #
    # Returns    : The output from the SQL statement.
    #
    # Parameters : $sql - the SQL statement to execute.
    #
    # Throws     : Any exception thrown by subroutines that this one calls.
    sub _execute_sql {
        my ( $self, $sql ) = @_;
        my @cmd = ( $self->_build_command_prefix(), '--execute', $sql );
        return $self->execute_command(@cmd);
    }

    ##
    # Usage      : @prefix = $obj->_build_command_prefix()
    #
    # Purpose    : Builds the command prefix common to all commands issued to
    #              MySQL.
    #
    # Returns    : The list of command arguments.
    #
    # Parameters : None.
    #
    # Throws     : No exceptions.
    sub _build_command_prefix {
        my ($self)   = @_;
        my $host     = $self->get_host();
        my $database = $self->get_database();
        return ( 'mysql', '-h', $host, '-D', $database );
    }

    ##
    # Usage      : $obj->_remove_table_header(\@lines);
    #
    # Purpose    : Removes the table header lines from the given array of
    #              output lines from a MySQL command.
    #
    # Returns    : Nothing.
    #
    # Parameters : $lines_ref - a reference to the array of output lines.
    #
    # Throws     : No exceptions.
    sub _remove_table_header {
        my ( $self, $lines_ref ) = @_;
        splice @{$lines_ref}, 0, 1;
        return;
    }
}

1;
