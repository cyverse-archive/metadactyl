package PostgreSQL;

use warnings;
use strict;

use base qw(Database);

use Carp;
use English qw(-no_match_vars);

##
# Provides the basic methods required to be able to initialize a PostgreSQL
# database.
{
    ##
    # Usage      : $obj->validate_environment();
    #
    # Purpose    : Verifies that the environment includes everything required
    #              for this utility to be able to initialize a PostgreSQL
    #              database.
    #
    # Returns    : Nothing.
    #
    # Parameters : None.
    #
    # Throws     : Any exception thrown by subroutines that this one calls.
    sub validate_environment {
        my ($self) = @_;
        $self->verify_file_existence(
            { "$ENV{HOME}/.pgpass" => "PostgreSQL password file", } );
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
            $self->_execute_sql('drop schema public cascade');
            $self->_execute_sql('create schema public');
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
    # Throws     : "unable to change the database ownership: $reason"
    sub change_database_ownership {
        my ($self) = @_;
        my $user   = $self->get_user();
        my $db     = $self->get_database();
        eval {
            $self->_execute_sql("alter database $db owner to $user");
            $self->_execute_sql("alter schema public owner to $user");
        };
        croak "unable to change the database ownership: $EVAL_ERROR"
            if $EVAL_ERROR;
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
    # Usage      : @prefix = $obj->_build_command_prefix()
    #
    # Purpose    : Builds the command prefix common to all commands issued to
    #              PostgreSQL.
    #
    # Returns    : The list of command arguments.
    #
    # Parameters : None.
    #
    # Throws     : No exceptions.
    sub _build_command_prefix {
        my ($self) = @_;
        my $host   = $self->get_host();
        my $user   = $self->get_user();
        my $db     = $self->get_database();
        return 'psql', '-h', $host, '-U', $user, '-d', $db;
    }

    ##
    # Usage      : $obj->_execute_sql($sql);
    #
    # Purpose    : Executes an SQL statement.
    #
    # Returns    : Nothing.
    #
    # Parameters : $sql - the SQL statement to execute.
    #
    # Throws     : Any exception thrown by subroutines that this one calls.
    sub _execute_sql {
        my ( $self, $sql ) = @_;
        $self->execute_command( $self->_build_command_prefix, '-c', $sql );
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
        $self->execute_command( $self->_build_command_prefix, '-f', $file );
        return;
    }
}

1;
