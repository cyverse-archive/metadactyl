package Database;

use warnings;
use strict;

use Carp;
use English qw(-no_match_vars);
use Scalar::Util qw(refaddr);

##
# Provides the basic initialization algorithm for all supported database
# management systems.
{
    my %host_for;
    my %sql_dir_for;
    my %user_for;
    my %database_for;
    my %scripts_for;

    ##
    # Usage      : $instance = $package->new(
    #                  {   'host'     => $host,
    #                      'sql_dir'  => $directory,
    #                      'user'     => $username,
    #                      'database' => $database,
    #                      'scripts'  => \@scripts,
    #                  }
    #              );
    #
    # Purpose    : Creates a new class instance.
    #
    # Returns    : A reference to the class instance.
    #
    # Parameters : host     - the host where the DBMS resides.
    #              sql_dir  - the directory contaning the SQL scripts.
    #              user     - the username to use for database authentication.
    #              database - the name of the database to use.
    #              scripts  - the list of SQL scripts.
    #
    # Throws     : No exceptions.
    #
    # Comments   : The value of $package in the example usage should be the
    #              name of the specific subclass of this class that is being
    #              instantiated.
    sub new {
        my ( $class, $args_ref ) = @_;
        my $self = bless \do { my $anon_scalar }, $class;
        $host_for{ refaddr $self }     = $args_ref->{'host'};
        $sql_dir_for{ refaddr $self }  = $args_ref->{'sql_dir'};
        $user_for{ refaddr $self }     = $args_ref->{'user'};
        $database_for{ refaddr $self } = $args_ref->{'database'};
        $scripts_for{ refaddr $self }  = $args_ref->{'scripts'};
        return $self;
    }

    ##
    # Usage      : N/A
    #
    # Purpose    : Cleans up after an instance of the class has been deleted.
    #
    # Returns    : Nothing.
    #
    # Parameters : None.
    #
    # Throws     : No exceptions.
    #
    # Comments   : All instance properties should be deleted in order to avoid
    #              memory leaks.
    sub DESTROY {
        my ($self) = @_;
        delete $host_for{ refaddr $self };
        delete $sql_dir_for{ refaddr $self };
        delete $user_for{ refaddr $self };
        delete $database_for{ refaddr $self };
        delete $scripts_for{ refaddr $self };
        return;
    }

    ##
    # Usage      : $host = $obj->get_host();
    #
    # Purpose    : The getter for the 'host' property.
    #
    # Returns    : The value of the 'host' property.
    #
    # Parameters : None.
    #
    # Throws     : No exceptions.
    sub get_host {
        my ($self) = @_;
        return $host_for{ refaddr $self };
    }

    ##
    # Usage      : $sql_dir = $obj->get_sql_dir()
    #
    # Purpose    : The getter for the 'sql_dir' property.
    #
    # Returns    : The value of the 'sql_dir' property.
    #
    # Parameters : None.
    #
    # Throws     : No exceptions.
    sub get_sql_dir {
        my ($self) = @_;
        return $sql_dir_for{ refaddr $self };
    }

    ##
    # Usage      : $user = $obj->get_user();
    #
    # Purpose    : The getter for the 'user' property.
    #
    # Returns    : The value of the 'user' property.
    #
    # Parameters : None.
    #
    # Throws     : No exceptions.
    sub get_user {
        my ($self) = @_;
        return $user_for{ refaddr $self };
    }

    ##
    # Usage      : $database = $obj->get_database();
    #
    # Purpose    : The getter for the 'database' property.
    #
    # Returns    : The value of the 'database' property.
    #
    # Parameters : None.
    #
    # Throws     : No exceptions.
    sub get_database {
        my ($self) = @_;
        return $database_for{ refaddr $self };
    }

    ##
    # Usage      : $scripts_ref = $obj->get_scripts();
    #
    # Purpose    : The getter for the 'scripts' property.
    #
    # Returns    : A reference to an array of database initialization scripts.
    #
    # Parameters : None.
    #
    # Throws     : No exceptions.
    sub get_scripts {
        my ($self) = @_;
        return $scripts_for{ refaddr $self };
    }

    ##
    # Usage      : $obj->initialize();
    #
    # Purpose    : Performs the database initialization.  This method uses the
    #              template method pattern to define the basic algorithm used
    #              to initialize databases for all database management
    #              systems.
    #
    # Returns    : Nothing.
    #
    # Parameters : None.
    #
    # Throws     : Any exception thrown by subroutines that this one calls.
    sub initialize {
        my ($self) = @_;
        $self->validate_environment();
        $self->clear_database();
        $self->change_database_ownership();
        $self->run_database_scripts();
        return;
    }

    ##
    # Usage      : $obj->verify_file_existence(
    #                  {   $file1 => $description1,
    #                      $file2 => $description2,
    #                      ...,
    #                      $filen => $descriptionn,
    #                  }
    #              );
    #
    # Purpose    : Verifies the existence of one or more files.  The files are
    #              specified by passing a hash reference to the subroutine in
    #              which the keys are the file names and the values are the
    #              descriptions of the corresponding files.
    #
    # Returns    : Nothing.
    #
    # Parameters : $args_ref - a reference to the hash described above.
    #
    # Throws     : "$desc, $file, not found"
    sub verify_file_existence {
        my ( $self, $args_ref ) = @_;
        for my $file ( keys %{$args_ref} ) {
            my $desc = $args_ref->{$file};
            croak "$desc, $file, not found"
                if !-e $file;
        }
        return;
    }

    ##
    # Usage      : @output = $obj->execute_command(@cmd);
    #
    # Purpose    : Executes a command and returns the output from the command
    #              as an array of lines.
    #
    # Returns    : The output from the command.
    #
    # Parameters : @cmd - the command to execute.
    #
    # Throws     : "unable to execute @cmd: $reason"
    #              "unable to execute @cmd"
    sub execute_command {
        my ( $self, @cmd ) = @_;
        open my $pipe, '-|', @cmd
            or croak "unable to execute @cmd: $ERRNO";
        my @lines = <$pipe>;
        close $pipe
            or croak "unable to execute @cmd: $ERRNO";
        croak "unable to execute @cmd"
            if $CHILD_ERROR;
        return @lines;
    }
}

1;
