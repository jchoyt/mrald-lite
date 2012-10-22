#!/usr/bin/perl -w 
# pathalizer
#
# Description:
# This program will create a graphml file out of all Apache common log
# sections of a log file that shows the path that people most commonly take
# through the site. External links will be ignored.
# 
# Programmer:       Paul Lockaby
# Compiler:         perl5 (revision 5.0 version 8 subversion 2)
# Usage:            pathalizer.pl --config=config_file, [--start=mm/dd/yyyy, ] [--end=mm/dd/yy, ] log_file, [log_file, [...,]]
#
# Parameters:
# config_file   = contains configuration directives governing the operation of
#                 this script.
# start-date    = the first date in a date range we want to look for. if not
#                 specified, we start from the beginning
# end-date      = the last date in a date range we want to look for. if not
#                 specified, we end at the end
# include-user  = show a graph of everything that "username" has done. if this
#                 flag is present without a value, it will show everything
#                 done by users without usernames specified, that is "-"
# exclude-user  = show a graph of everything done with everyone but the
#                 specified user name. if this flag is present without a
#                 value, it will remove everything without a username
#                 specified, that is "-"
# include-page  = show a graph of everything done with this page, which is
#                 actually rather useless. if this flag is present without a
#                 value, it is assumed to refer to the root directory, that
#                 is "/"
# exclude-page  = show a graph of everything done excepting this page, which
#                 turns out to be rather useful. if this flag is present
#                 without a value, it is assumed to refer to the root
#                 directory, that is "/"
# log_file      = an arbitrary number of file names in no particular order in
#                 either Apache common or Apache combined log format.
#
# Note On Combining includes/excludes:
# Includes and excludes are compared simultaneously using logical ANDS. This
# means that everything you specify has to match. So if you want to include
# /journal and exclude /gallery for users bob and joe, but not mary, then
# these would not match:
#
# 172.19.84.21 mary - 04/May/2005:14:04:31 -0400 GET /journal/ HTTP/1.1 200 216
# 172.19.84.21 bob - 04/May/2005:14:05:31 -0400 GET / HTTP/1.1 200 216
# 172.19.84.21 joe - 04/May/2005:14:06:31 -0400 GET /journal/ HTTP/1.1 200 216
# 172.19.84.21 - - 04/May/2005:14:07:31 -0400 GET /gallery/ HTTP/1.1 200 216
#
# But these would match:
#
# 172.19.84.21 bob - 04/May/2005:14:04:31 -0400 GET /journal/ HTTP/1.1 200 216
# 172.19.84.21 joe - 04/May/2005:14:05:31 -0400 GET /journal/ HTTP/1.1 200 216
#

use strict;
#use warnings;
use Time::Local;
use Getopt::Long;

if ($#ARGV < 1) {
	die "USAGE: pathalizer.pl --config=config_file, [--include-user=username, [...,]|--exclude-user=username, [...,]] [--start-date=mm/dd/yyyy, ] [--end-date=mm/dd/yy, ] [--exclude-page=url, [..., ]|--include-page=url, [..., ]] log_file, [log_file, [...,]]\n";
}

my $config;
my $start_date;
my $end_date;
my @include_user;
my @exclude_user;
my @exclude_page;
my @include_page;
GetOptions(
	"config=s" => \$config,
	"start-date:s" => \$start_date,
	"include-user:s" => \@include_user,
	"exclude-user:s" => \@exclude_user,
	"include-page:s" => \@include_page,
	"exclude-page:s" => \@exclude_page,
	"end-date:s" => \$end_date
);

if (!defined($config)) {
	die "You must specify a config file.\n";
}
if (defined($start_date) && $start_date eq "") {
	die "You used the start date option, but did not specify a start date.\n";
}
if (defined($end_date) && $end_date eq "") {
	die "You used the end date option, but did not specify an end date.\n";
}
if (defined($start_date) && $start_date !~ m/\d{1,2}\/\d{1,2}\/\d{4}/) {
	# verify that the date is in the right format
	die "Start date is not in right format. Must be mm/dd/yyyy.\n";
}
if (defined($end_date) && $end_date !~ m/\d{1,2}\/\d{1,2}\/\d{4}/) {
	# verify that the end date is in the right format
	die "End date is not in right format. Must be mm/dd/yyyy.\n";
}

# Check to see that the log exists, die if not
if (!(-e $config)) {
	die "$config does not exist.\n";
}
if (!(-r $config)) {
	die "$config cannot be read.\n";
}

my @files = ();
for (my $i = 0; $i <= $#ARGV; $i++) {
	if (!(-e $ARGV[$i])) {
		die "$ARGV[$i] does not exist.\n";
	}
	if (!(-r $ARGV[$i])) {
		die "$ARGV[$i] cannot be read.\n";
	}
	push(@files, $ARGV[$i]);
}

# load the configuration file
my @unify = ();				# pages that are equal
my @ignore = ();			# pages that are to be ignored
my $timeout = -1;			# how long a session lasts, default is no timeout
my $min_edgewidth = -1;			# minimal threshold for displaying an edge
my $max_edgecount = 30;			# the maximum number of edges to show if edgewidth is automatic
my %colors = ();			# these are colors that will be applied to page types

open(CONF, "< $config");
while(defined (my $line = <CONF>)) {
	chomp $line;			# no newline
	$line =~ s/\s*^#.*//;		# remove comments at the beginning of a line
	$line =~ s/^\s+|\s+$//;		# trim spacing
	next unless length($line);	# anything left?

	$line =~ m/^(\w+) /;		# match the first word
	my $option = $1;

	if ($option eq "ignore") {
		$line =~ m/^$option (?:\"(.*?)\")$/;
		push(@ignore, $1);
	}	
	if ($option eq "timeout") {
		$line =~ m/^$option (-?\d+)/;
		$timeout = $1;
	}
	if ($option eq "min_edgewidth") {
		$line =~ m/^$option (-?\d+)/;
		$min_edgewidth = $1;
	}
	if ($option eq "max_edgecount") {
		$line =~ m/^$option (-?\d+)/;
		$max_edgecount = $1;
	}
	if ($option eq "color") {
		$line =~ m/^$option (?:\"(.*?)\") (?:\"(.*?)\")$/;
		$colors{$1} = $2;
	}
}
close(CONF);

my %sessions = ();

{
	my @edges = ();
	foreach my $logfile (@files) {
		# Open the log file
		open(LOG, "< $logfile");

		# loop through the file
		while(<LOG>) {
			chomp;							# remove blank lines
			next unless length;					# loop to find the next non-empty line

			# parse this line, assuming it to be in combined format
			my ($ip, $username, $password, $day, $month, $year, $hour, $minute, $second, $tz, $method, $url, $protocol, $response, $size)
				= m#([^ ]+) ([^ ]+) ([^ ]+) ([^/]+)/([^/]+)/([^:]+)\:([^:]{2})\:([^:]{2})\:([^ ]{2}) ([^ ]{5,6}) ([^ ]+) ([^ ^?]+)[^ ]* ([^ ]+) ([^ ]+) ([^ ]+)#;
			
			next unless $size;					# if there is no file size then we don't want to look at this
			next unless ($response !~ m/301|4..|5../);		# if we got an error code or a permanent redirect, then we don't want to look at this
			next unless ($method =~ m/get|post/i);		# we don't want anything other than these methods of retrieval

			if (@include_user || @exclude_user || @include_page || @exclude_page) {
				my $include_user_string = join("|", @include_user);
				my $exclude_user_string = join("|", @exclude_user);
				my $include_page_string = join("|", @include_page);
				my $exclude_page_string = join("|", @exclude_page);

				if (!@include_user && !@exclude_user && !@include_page && @exclude_page) {
					next if ($url =~ m!$exclude_page_string!);
				}
				if (!@include_user && !@exclude_user && @include_page && !@exclude_page) {
					next if ($url !~ m!$include_page_string!);
				}
				if (!@include_user && !@exclude_user && @include_page && @exclude_page) {
					next if ($url !~ m!$include_page_string! && $url =~ m!$exclude_page_string!);
				}
				if (!@include_user && @exclude_user && !@include_page && !@exclude_page) {
					next if ($username =~ m!$exclude_user_string!);
				}
				if (!@include_user && @exclude_user && !@include_page && @exclude_page) {
					next if ($username =~ m!$exclude_user_string! && $url =~ m!$exclude_page_string!);
				}
				if (!@include_user && @exclude_user && @include_page && !@exclude_page) {
					next if ($username =~ m!$exclude_user_string! && $url !~ m!$include_page_string!);
				}
				if (!@include_user && @exclude_user && @include_page && @exclude_page) {
					next if ($username =~ m!$exclude_user_string! && $url =~ m!$include_page_string! && $url !~ m!$exclude_page_string!);
				}
				if (@include_user && !@exclude_user && !@include_page && !@exclude_page) {
					next if ($username !~ m!$include_user_string!);
				}
				if (@include_user && !@exclude_user && !@include_page && @exclude_page) {
					next if ($username !~ m!$include_user_string! && $url =~ m!$exclude_page_string!);
				}
				if (@include_user && !@exclude_user && @include_page && !@exclude_page) {
					next if ($username !~ m!$include_user_string! && $url !~ m!$include_user_string!);
				}
				if (@include_user && !@exclude_user && @include_page && @exclude_page) {
					next if ($username !~ m!$include_user_string! && $url !~ m!$include_page_string! && $url =~ m!$exclude_page_string!);
				}
				if (@include_user && @exclude_user && !@include_page && !@exclude_page) {
					next if ($username =~ m!$exclude_user_string! && $username !~ m!$include_user_string!);
				}
				if (@include_user && @exclude_user && !@include_page && @exclude_page) {
					next if ($username !~ m!$include_user_string! && $username =~ m!$exclude_user_string! && $url =~ m!$exclude_page_string!);
				}
				if (@include_user && @exclude_user && @include_page && !@exclude_page) {
					next if ($username !~ m!$include_user_string! && $username =~ m!$exclude_user_string! && $url !~ m!$include_page_string!);
				}
				if (@include_user && @exclude_user && @include_page && @exclude_page) {
					next if ($username !~ m!$include_user_string! && $username =~ m!$exclude_user_string! && $url !~ m!$include_page_string! && $url =~ m!$exclude_page_string!);
				}
			}

			# skip any line that is in the ignore list
			my $skipList = "";
			foreach (@ignore) {
				$skipList .= "$_|";
			}
			substr($skipList, -1) = "";
			next unless ($url !~ m/(?:$skipList)/);

			my $time = timelocal($second, $minute, $hour, $day, (whatMonth($month) - 1), ($year - 1900));

			# record the nodes and edges in their own lists
			# also, we don't want to record the non-existant referrer
			push(@edges, [ $ip, $time, $url ]);
		}
		close(LOG);
	}

	my @temp = ();
	# remove any elements that are before the date we want
	if (defined($start_date)) {
		$start_date =~ m/(\d{1,2})\/(\d{1,2})\/(\d{4})/;
		my $epoch_start_date = timelocal(0, 0, 0, $2, ($1 - 1), ($3 - 1900));
		@edges = grep((@$_[1] > $epoch_start_date), @edges);
	}

	# remove any elements that are after the date we want
	if (defined($end_date)) {
		$end_date =~ m/(\d{1,2})\/(\d{1,2})\/(\d{4})/;
		my $epoch_end_date = timelocal(0, 0, 0, ($2 + 1), ($1 - 1), ($3 - 1900));
		@edges = grep((@$_[1] < $epoch_end_date), @edges);
	}

	# sort the data
	@edges = sort { @$a[1] <=> @$b[1] } @edges;

	# compile the data into sessions
	foreach my $item (@edges) {
		if (exists($sessions{@$item[0]}) && $#{$sessions{@$item[0]}} >= 0) {
			# compare this node to the last node, assume the log file is in chronological order
			# 1. see if it is in the same session by comparing IP address and time
			# 2. create an edge based off of the idea that if it's in the same session, then it's going to come from the last URL
			#    - if it's not in the same session, create a "new" edge, if it is in the same session, create an edge with the last URL as the source and this URL as the sink
			if ($timeout >= 0 && ((@$item[1] - $sessions{@$item[0]}[$#{$sessions{@$item[0]}}][0]) > ($timeout * 60))) {
				# this is not the same session
				push(@{$sessions{@$item[0]}}, [ @$item[1], "-", @$item[2] ]);
			} else {
				# this is the same session
				push(@{$sessions{@$item[0]}}, [ @$item[1], $sessions{@$item[0]}[$#{$sessions{@$item[0]}}][2], @$item[2]]);
			}
		} else {
			push(@{$sessions{@$item[0]}}, [ @$item[1], "-", @$item[2] ]);
		}
	}
	undef(@edges);
}

# output the new graphml file
print "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
print "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">\n";
print "<graph edgedefault=\"directed\">\n";

print "<key id=\"label\" for=\"node\" attr.name=\"label\" attr.type=\"string\"></key>\n";
print "<key id=\"shape\" for=\"node\" attr.name=\"shape\" attr.type=\"string\"></key>\n";
print "<key id=\"color\" for=\"node\" attr.name=\"color\" attr.type=\"string\"></key>\n";
print "<key id=\"weight\" for=\"edge\" attr.name=\"weight\" attr.type=\"int\"></key>\n";
print "<key id=\"label\" for=\"edge\" attr.name=\"label\" attr.type=\"string\"></key>\n";
		
# count up duplicate edges, print them, and print their totals
{
	# count up node edges
	my %seen = ();
	while(my($k, $v) = each %sessions) {
		foreach my $item (@$v) {
			if (exists($seen{@$item[1] . @$item[2]})) {
				$seen{@$item[1] . @$item[2]}[0]++;
			} else {
				$seen{@$item[1] . @$item[2]} = [ 1, @$item[1], @$item[2] ];
			}
		}
	}

	# sort by weight of edges
	# that is, sort the values of %seen
	my $count = 0;
	my @sordid = sort { $seen{$b} <=> $seen{$a} } keys %seen;
	my %unikey = ();
	my @edges = ();

	foreach my $item (@sordid) {
		if (($min_edgewidth < 0 && $count < $max_edgecount) || ($min_edgewidth >= 0 && $seen{$item}[0] > $min_edgewidth)) {
			push(@edges, [ $seen{$item}[1], $seen{$item}[2], $seen{$item}[0] ]);
			if (defined($unikey{$seen{$item}[1]}) && ($unikey{$seen{$item}[1]} eq "diamond" || $unikey{$seen{$item}[1]} eq "ellipse")) {
				$unikey{$seen{$item}[1]} = "ellipse";
			} else {
				$unikey{$seen{$item}[1]} = "octagon";
			}
			if (defined($unikey{$seen{$item}[2]}) && ($unikey{$seen{$item}[2]} eq "octagon" || $unikey{$seen{$item}[2]} eq "ellipse")) {
				$unikey{$seen{$item}[2]} = "ellipse";
			} else {
				$unikey{$seen{$item}[2]} = "diamond";
			}
		}
		$count++;
	}

	while(my($k, $v) = each %unikey) {
		
		print "\t<node id=\"" . $k . "\">\n";
		print "\t\t<data key=\"label\">" . $k . "</data>\n";
		print "\t\t<data key=\"shape\">" . $v . "</data>\n";
			my $found = 0;
			while((my($extension, $color) = each %colors) && !$found) {
				if ($k =~ m/(?:$extension)/) {
					print "\t\t<data key=\"color\">" . $color . "</data>\n";
					$found = 1;
				}
			}
		print "\t</node>\n";
	}

	foreach my $edge (@edges) {
		print "\t<edge directed=\"true\" source=\"" . @$edge[0] . "\" target=\"" . @$edge[1] . "\" weight=\"" . @$edge[2] . "\">\n";
		print "\t\t<data key=\"label\">" . @$edge[0] . "</data><data key=\"weight\">" . @$edge[2] . "</data>\t</edge>\n";
	}
}

print "</graph></graphml>\n";

###############################################################################
# whatMonth                                                                   #
# Returns the numeric equivalent to the month name that we pass it.           #
###############################################################################
sub whatMonth {
	my $name = shift;

	if ($name =~ /^j/i) {
		if ($name =~ /^ja/i) {
			return 1;			# january
		} elsif ($name =~ /^ju/i) {
			if ($name =~ /^jun/i) {
				return 6;		# june
			} elsif ($name =~ /^jul/i) {
				return 7;		# july
			}
		}
	} elsif ($name =~ /^f/i) {
		return 2;				# february
	} elsif ($name =~ /^m/i) {
		if ($name =~ /^mar/i) {
			return 3;			# march
		} elsif ($name =~ /^may/i) {
			return 5;			# may
		}
	} elsif ($name =~ /^a/i) {
		if ($name =~ /^ap/i) {
			return 4;			# april
		} elsif ($name =~ /^au/i) {
			return 8;			# august
		}
	} elsif ($name =~ /^s/i) {
		return 9;				# september
	} elsif ($name =~ /^o/i) {
		return 10;				# october
	} elsif ($name =~ /^n/i) {
		return 11;				# november
	} elsif ($name =~ /^d/i) {
		return 12;				# december
	}
	return 0;
}
